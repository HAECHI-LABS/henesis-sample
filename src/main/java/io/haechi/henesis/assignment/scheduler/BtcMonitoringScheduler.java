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

    public BtcMonitoringScheduler(
            HenesisClient henesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository,
            BalanceManager balanceManager,
            int pollingSize
    ) {
        this.henesisClient = henesisClient;
        this.depositAddressRepository = depositAddressRepository;
        this.transferRepository = transferRepository;
        this.balanceManager = balanceManager;
        this.pollingSize = pollingSize;
    }

    /*
    Advanced
    1. Bunch sync
    2. reorg handling
    3. dynamically set scheduler using TaskScheduler
     */
    @Transactional
    @Scheduled(fixedRate = 1800, initialDelay = 2000)
    public void updateTransactions() {
        LocalDateTime lastUpdatedAt = this.transferRepository.findTopByBlockchainAndStatusOrderByHenesisUpdatedAtDesc(Blockchain.BITCOIN, Transfer.Status.CONFIRMED)
                .map(Transfer::getUpdatedAt)
                .map(time -> time.plusNanos(1))
                .orElse(Utils.toLocalDateTime(Long.toString(System.currentTimeMillis())));

        this.henesisClient.getLatestTransfersByUpdatedAtGte(lastUpdatedAt, this.pollingSize)
                .stream()
                .map(henesisTransfer -> {
                    Transfer localTransfer = this.transferRepository.findByHenesisTransactionIdAndType(henesisTransfer.getHenesisTransactionId(), henesisTransfer.getType())
                            .orElse(null);
                    if (localTransfer == null) {
                        return henesisTransfer;
                    }
                    localTransfer.updateStatus(henesisTransfer.getStatus());
                    return localTransfer;
                })
                .map(this.transferRepository::save)
                .filter(Transfer::isConfirmed)
                .forEach(transfer -> {
                    DepositAddress depositAddress = null;
                    if (transfer.isWithdrawal() && transfer.isMasterWallet()) {
                        if (!transfer.isWithdrawnFromDepositAddress()) {
                            return;
                        }
                        depositAddress = this.depositAddressRepository.findById(transfer.getDepositAddressId())
                                .orElseThrow(() -> new IllegalStateException(String.format("there is no '%s' deposit address", transfer.getDepositAddressId())));
                    }

                    if (transfer.isDeposit() && transfer.isDepositAddress()) {
                        depositAddress = this.depositAddressRepository.findByAddress(transfer.getTo())
                                .orElseThrow(() -> new IllegalStateException(String.format("there is no '%s' deposit address", transfer.getTo())));
                    }

                    if (transfer.isDeposit() && transfer.isMasterWallet()) {
                        return;
                    }

                    this.balanceManager.reflectTransfer(transfer, depositAddress);
                });
    }
}
