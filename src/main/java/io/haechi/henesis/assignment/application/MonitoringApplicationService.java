package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitoringApplicationService {

    private final WalletService walletService;
    private final UserWalletRepository userWalletRepository;
    private final FlushedTxRepository flushedTxRepository;

    public MonitoringApplicationService(WalletService walletService,
                                      UserWalletRepository userWalletRepository,
                                      FlushedTxRepository flushedTxRepository) {
        this.walletService = walletService;
        this.userWalletRepository = userWalletRepository;
        this.flushedTxRepository = flushedTxRepository;
    }

    public void getValueTransferEvents(){
        List<Transaction> transactions = walletService.getValueTransferEvents();




        //입금 시나리오

         transactions.stream()
                .filter(tx-> flushedTxRepository.findByTxId(tx.getTransactionId()).isEmpty())
                .filter(d-> d.getTransferType().contains("DEPOSIT")&&d.getStatus().contains("CONFIRMED"))
                .map(b ->userWalletRepository.findByWalletId(b.getWalletId()).isPresent())
                .collect(Collectors.toList());

    }
}
