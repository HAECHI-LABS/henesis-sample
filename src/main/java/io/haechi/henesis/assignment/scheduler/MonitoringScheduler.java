package io.haechi.henesis.assignment.scheduler;

import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.TransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;

@Slf4j
public class MonitoringScheduler {
    private final HenesisClient henesisClient;
    private final DepositAddressRepository depositAddressRepository;
    private final TransferRepository transferRepository;
    private final Blockchain blockchain;

    public MonitoringScheduler(
            HenesisClient henesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository,
            Blockchain blockchain
    ) {
        this.henesisClient = henesisClient;
        this.depositAddressRepository = depositAddressRepository;
        this.transferRepository = transferRepository;
        this.blockchain = blockchain;
    }

    /*
    Advanced
    1. Bunch sync
    2. reorg handling
     */
    @Transactional
    @Scheduled(fixedRate = 2000, initialDelay = 500)
    public void updateTransactions() {
        String lastUpdatedAt = this.transferRepository.findTopByBlockchainOrderByHenesisUpdatedAt(this.blockchain)
                .map(Transfer::getUpdatedAt)
                .orElse(Long.toString(System.currentTimeMillis()));

        this.henesisClient.getLatestTransfersByUpdatedAtGte(lastUpdatedAt)
                .stream()
                .map(henesisTransfer -> {
                    Transfer localTransfer = this.transferRepository.findByHenesisId(henesisTransfer.getHenesisId()).orElse(null);
                    if (localTransfer == null) {
                        return henesisTransfer;
                    }
                    localTransfer.updateStatus(henesisTransfer.getStatus());
                    return localTransfer;
                })
                .filter(Transfer::isConfirmed) // TODO: when occurs reorg
                .forEach(transfer -> {
                    String address = transfer.isDeposit() ? transfer.getTo() : transfer.getFrom();
                    DepositAddress depositAddress = this.depositAddressRepository.findByAddress(address)
                            .orElseThrow(() -> new IllegalStateException(String.format("there is no '%s' deposit address", address)));

                    if (transfer.isDeposit()) {
                        depositAddress.deposit(transfer.getAmount());
                        return;
                    }
                    depositAddress.withdrawal(transfer.getAmount());
                });
    }

    //지갑 상태 업데이트 (CREATING, ACTIVE, INACTIVE)
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
