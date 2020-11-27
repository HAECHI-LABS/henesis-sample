package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.*;
import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.repository.FlushedTxRepository;
import io.haechi.henesis.assignment.domain.repository.UserWalletRepository;

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
                .name(userWallet.getWalletName())
                .createdAt(userWallet.getCreatedAt())
                .blockchain(userWallet.getBlockchain())
                .address(userWallet.getWalletAddress())
                .id(userWallet.getWalletId())
                .build();
    }

    @Transactional
    public TransferResponseDTO transfer(TransferRequestDTO request) {

        Amount spendableAmount = walletService.getMasterWalletBalance(request.getTicker());
        UserWallet userWallet = userWalletRepository.getUserWalletByWalletId(request.getUserWalletId());

        if (spendableAmount.compareTo(userWallet.getWalletBalance()) < 0
                && spendableAmount.compareTo(request.getAmount()) < 0) {
            throw new IllegalStateException("No Money");
        } else {
            walletService.transfer(
                    request.getAmount(),
                    request.getTo(),
                    request.getTicker()
            );

            userWallet.getWalletBalance().subtract(request.getAmount());
            userWalletRepository.save(userWallet);

        }
        return TransferResponseDTO.builder()
                .walletName(userWallet.getWalletName())
                .amount(request.getAmount())
                .walletBalance(userWallet.getWalletBalance())
                .build();
    }

    @Transactional
    public FlushResponseDTO flush(FlushRequestDTO request) {

        List<String> userWalletIds = walletService.getUserWalletIds();
        System.out.println("Get All User Wallet Id : " + userWalletIds);

        Transaction transaction = walletService.flushAll(
                request.getTicker(),
                userWalletIds
        );

        flushedTxRepository.save(new FlushedTx(transaction));

        return FlushResponseDTO.builder()
                .txId(transaction.getTxId())
                .blockchain(transaction.getBlockchain())
                .build();
    }

}
