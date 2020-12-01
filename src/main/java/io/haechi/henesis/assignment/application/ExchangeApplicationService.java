package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.*;
import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.transaction.Transaction;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ExchangeApplicationService {

    private final WalletService walletService;
    private final UserWalletRepository userWalletRepository;
    private final FlushedTxRepository flushedTxRepository;

    public ExchangeApplicationService(WalletService walletService,
                                      UserWalletRepository userWalletRepository,
                                      FlushedTxRepository flushedTxRepository) {
        this.walletService = walletService;
        this.userWalletRepository = userWalletRepository;
        this.flushedTxRepository = flushedTxRepository;
    }


    @Transactional
    public CreateWalletResponseDTO createUserWallet(CreateWalletRequestDTO request) {

        UserWallet userWallet = walletService.createUserWallet(request.getWalletName());
        userWalletRepository.save(userWallet);

        return CreateWalletResponseDTO.builder()
                .name(userWallet.getName())
                .createdAt(userWallet.getCreatedAt())
                .blockchain(userWallet.getBlockchain())
                .address(userWallet.getAddress())
                .id(userWallet.getWalletId())
                .build();
    }

    @Transactional
    public TransferResponseDTO transfer(TransferRequestDTO request) {

        UserWallet userWallet = userWalletRepository.findByWalletId(request.getUserWalletId())
                .orElseThrow(()-> new IllegalArgumentException(String.format("Can Not Found User wallet %s", request.getUserWalletId())));

        Amount spendableAmount = walletService.getMasterWalletBalance(request.getTicker());
        Amount walletBalance = userWallet.getBalance();
        Amount amount = request.getAmount();


        if (spendableAmount.compareTo(walletBalance) < 0
                && spendableAmount.compareTo(request.getAmount()) < 0) {
            throw new IllegalStateException("Not Enough Money..!");
        }

        Transaction transaction = walletService.transfer(
                request.getAmount(),
                request.getTo(),
                request.getTicker()
        );


        walletBalance.subtract(amount);
        userWalletRepository.save(userWallet);

        return TransferResponseDTO.of(
                transaction.getWalletName(),
                transaction.getAmount(),
                walletBalance
        );
    }

    @Transactional
    public FlushResponseDTO flush(FlushRequestDTO request) {

        List<String> userWalletIds = walletService.getUserWalletIds();
        System.out.println("Get All User Wallet Id : " + userWalletIds);

        Transaction transaction = walletService.flushAll(
                request.getTicker(),
                userWalletIds
        );

        flushedTxRepository.save(
                FlushedTx.of(
                    transaction.getTransactionId(),
                    transaction.getBlockchain(),
                    transaction.getStatus(),
                    transaction.getCreatedAt()
                )
        );

        return FlushResponseDTO.builder()
                .txId(transaction.getTransactionId())
                .blockchain(transaction.getBlockchain())
                .build();
    }

}
