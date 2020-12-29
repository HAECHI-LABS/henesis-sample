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
    3. dynamically set scheduler using TaskScheduler
     */
    @Transactional
    @Scheduled(fixedRate = 5000, initialDelay = 2000)
    public void updateTransactions() {
        LocalDateTime lastUpdatedAt = this.transferRepository.findTopByBlockchainAndStatusOrderByHenesisUpdatedAtDesc(this.blockchain, Transfer.Status.CONFIRMED)
                .map(Transfer::getUpdatedAt)
                .map(time -> time.plusNanos(1))
                .orElse(Utils.toLocalDateTime(Long.toString(System.currentTimeMillis())));

        this.henesisClient.getLatestTransfersByUpdatedAtGte(lastUpdatedAt, this.pollingSize)
                .stream()
                .peek(this::updateFlushedTransfer)
                .map(henesisTransfer -> {
                    Transfer localTransfer = this.transferRepository.findByHenesisTransactionIdAndType(henesisTransfer.getHenesisTransactionId(), henesisTransfer.getType())
                            .orElse(null);
                    if (localTransfer == null) {
                        return henesisTransfer;
                    }
                    localTransfer.updateHenesisContext(henesisTransfer);
                    return localTransfer;
                })
                .map(this.transferRepository::save)
                .filter(Transfer::isConfirmed)
                .forEach(transfer -> {
                    DepositAddress depositAddress = null;
                    if (transfer.isWithdrawal() && transfer.isMasterWallet()) {
                        // deposit id가 없으면 master wallet 에서 실제로 출금이 일어난 것
                        if (!transfer.isWithdrawnFromDepositAddress()) {
                            return;
                        }
                        depositAddress = this.depositAddressRepository.findById(transfer.getDepositAddressId())
                                .orElseThrow(() -> new IllegalStateException(String.format("there is no '%s' deposit address", transfer.getDepositAddressId())));
                    }

                    if (transfer.isWithdrawal() && transfer.isDepositAddress()) {
                        // flush의 경우 차감하지 않는다.
                        if (this.transferRepository.existsByBlockchainAndHenesisTransactionIdAndType(this.blockchain, transfer.getHenesisTransactionId(), Transfer.Type.FLUSH)) {
                            return;
                        }
                        depositAddress = this.depositAddressRepository.findByAddress(transfer.getFrom())
                                .orElseThrow(() -> new IllegalStateException(String.format("there is no '%s' deposit address", transfer.getFrom())));
                    }

                    if (transfer.isDeposit() && transfer.isDepositAddress()) {
                        // deposit address로의 입금 반영
                        depositAddress = this.depositAddressRepository.findByAddress(transfer.getTo())
                                .orElseThrow(() -> new IllegalStateException(String.format("there is no '%s' deposit address", transfer.getTo())));
                    }

                    if (transfer.isDeposit() && transfer.isMasterWallet()) {
                        return;
                    }

                    this.balanceManager.reflectTransfer(transfer, depositAddress);
                });
    }

    @Transactional
    @Scheduled(fixedRate = 5000, initialDelay = 2000)
    public void updateWalletStatus() {
        this.depositAddressRepository.findAllByBlockchainAndStatus(this.blockchain, DepositAddress.Status.CREATING)
                .forEach(depositAddress -> {
                    DepositAddress fromHenesis = this.henesisClient.getDepositAddress(depositAddress.getHenesisId());
                    depositAddress.updateStatus(fromHenesis.getStatus());
                });
    }

    private void updateFlushedTransfer(Transfer henesisTransfer) {
        this.transferRepository.findByBlockchainAndHenesisTransactionIdAndType(this.blockchain, henesisTransfer.getHenesisTransactionId(), Transfer.Type.FLUSH)
                .ifPresent(matchedFlushedTransfer -> {
                    matchedFlushedTransfer.updateHenesisContext(henesisTransfer);
                    this.transferRepository.save(matchedFlushedTransfer);
                });
    }
}
