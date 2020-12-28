package io.haechi.henesis.assignment.scheduler;

import io.haechi.henesis.assignment.domain.BalanceManager;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.TransferRepository;
import io.haechi.henesis.assignment.support.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
public class MonitoringScheduler {
    private final HenesisClient henesisClient;
    private final DepositAddressRepository depositAddressRepository;
    private final TransferRepository transferRepository;
    private final BalanceManager balanceManager;
    private final Blockchain blockchain;
    private final int pollingSize;

    public MonitoringScheduler(
            HenesisClient henesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository,
            BalanceManager balanceManager,
            Blockchain blockchain,
            int pollingSize
    ) {
        this.henesisClient = henesisClient;
        this.depositAddressRepository = depositAddressRepository;
        this.transferRepository = transferRepository;
        this.balanceManager = balanceManager;
        this.blockchain = blockchain;
        this.pollingSize = pollingSize;
    }

    /*
    Advanced
    1. Bunch sync
    2. reorg handling
     */
    @Transactional
    @Scheduled(fixedRate = 2000, initialDelay = 500)
    public void updateTransactions() {
        LocalDateTime lastUpdatedAt = this.transferRepository.findTopByBlockchainOrderByHenesisUpdatedAtDesc(this.blockchain)
                .map(Transfer::getUpdatedAt)
                .map(time -> time.plusNanos(1))
                .orElse(Utils.toLocalDateTime(Long.toString(System.currentTimeMillis())));

        this.henesisClient.getLatestTransfersByUpdatedAtGte(lastUpdatedAt, this.pollingSize)
                .stream()
                .map(henesisTransfer -> {
                    Transfer localTransfer = this.transferRepository.findByHenesisId(henesisTransfer.getHenesisId()).orElse(null);
                    if (localTransfer == null) {
                        return henesisTransfer;
                    }
                    localTransfer.updateStatus(henesisTransfer.getStatus());
                    return localTransfer;
                })
                .filter(transfer -> !transfer.isFlushed())
                .filter(Transfer::isConfirmed) // TODO: when occurs reorg
                .forEach(transfer -> {
                    DepositAddress depositAddress = null;
                    if (transfer.isDeposit()) {
                        // TODO: 마스터지갑일 경우 제외
                        depositAddress = this.depositAddressRepository.findByAddress(transfer.getTo())
                                .orElseThrow(() -> new IllegalStateException(String.format("there is no '%s' deposit address", transfer.getTo())));
                    } else {
                        // TODO: deposit address에서 출금이 발생할 때
                        depositAddress = this.depositAddressRepository.findById(transfer.getDepositAddressId())
                                .orElseThrow(() -> new IllegalStateException("there is no '%s' deposit address"));
                    }
                    this.balanceManager.reflectTransfer(transfer, depositAddress);
                    this.transferRepository.save(transfer);
                });
    }

    @Async
    @Transactional
    @Scheduled(fixedRate = 6000, initialDelay = 2000)
    public void updateWalletStatus() {
        this.depositAddressRepository.findAllByBlockchainAndStatus(this.blockchain, DepositAddress.Status.CREATING)
                .forEach(depositAddress -> {
                    DepositAddress fromHenesis = this.henesisClient.getDepositAddress(depositAddress.getHenesisId());
                    depositAddress.updateStatus(fromHenesis.getStatus());
                });
    }
}
