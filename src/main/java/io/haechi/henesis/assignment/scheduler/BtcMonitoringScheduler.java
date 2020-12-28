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
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
public class BtcMonitoringScheduler {
    private final HenesisClient henesisClient;
    private final DepositAddressRepository depositAddressRepository;
    private final TransferRepository transferRepository;
    private final BalanceManager balanceManager;
    private final int pollingSize;
    private final String masterWalletAddress;

    public BtcMonitoringScheduler(
            HenesisClient henesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository,
            BalanceManager balanceManager,
            int pollingSize,
            String masterWalletAddress
    ) {
        this.henesisClient = henesisClient;
        this.depositAddressRepository = depositAddressRepository;
        this.transferRepository = transferRepository;
        this.balanceManager = balanceManager;
        this.pollingSize = pollingSize;
        this.masterWalletAddress = masterWalletAddress;
    }

    /*
    Advanced
    1. Bunch sync
    2. reorg handling
     */
    @Transactional
    @Scheduled(fixedRate = 2000, initialDelay = 500)
    public void updateTransactions() {
        LocalDateTime lastUpdatedAt = this.transferRepository.findTopByBlockchainOrderByHenesisUpdatedAtDesc(Blockchain.BITCOIN)
                .map(Transfer::getUpdatedAt)
                .map(time -> time.plusNanos(1))
                .orElse(Utils.toLocalDateTime(Long.toString(System.currentTimeMillis())));

        this.henesisClient.getLatestTransfersByUpdatedAtGte(lastUpdatedAt, this.pollingSize)
                .stream()
                .map(henesisTransfer -> {
                    Transfer localTransfer = this.transferRepository.findByHenesisTransferId(henesisTransfer.getHenesisTransferId()).orElse(null);
                    if (localTransfer == null) {
                        return henesisTransfer;
                    }
                    localTransfer.updateStatus(henesisTransfer.getStatus());
                    return localTransfer;
                })
                .filter(transfer -> !transfer.getTo().equals(this.masterWalletAddress)) // 마스터 지갑은 관리하지 않는다. 마스터 지갑 입금 내역은 제외
                .filter(Transfer::isConfirmed) // TODO: when occurs reorg
                .forEach(transfer -> {
                    DepositAddress depositAddress = this.depositAddressRepository.findByAddress(transfer.getTo())
                            .orElseThrow(() -> new IllegalStateException(String.format("there is no '%s' deposit address", transfer.getTo())));
                    this.balanceManager.reflectTransfer(transfer, depositAddress);
                    this.transferRepository.save(transfer);
                });
    }
}
