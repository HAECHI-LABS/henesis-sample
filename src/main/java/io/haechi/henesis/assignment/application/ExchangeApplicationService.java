package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.*;
import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.transaction.Transaction;
import io.haechi.henesis.assignment.domain.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeApplicationService {

    private final Exchange exchange;
    private final UserWalletRepository userWalletRepository;
    private final TransactionRepository transactionRepository;
    private final FlushedTxRepository flushedTxRepository;

    public ExchangeApplicationService(Exchange exchange,
                                      UserWalletRepository userWalletRepository,
                                      TransactionRepository transactionRepository,
                                      FlushedTxRepository flushedTxRepository) {
        this.exchange = exchange;
        this.userWalletRepository = userWalletRepository;
        this.transactionRepository =transactionRepository;
        this.flushedTxRepository = flushedTxRepository;
    }


    @Transactional
    public CreateWalletResponseDTO createUserWallet(CreateWalletRequestDTO request) {

        UserWallet userWallet = exchange.createUserWallet(request.getWalletName());
        userWalletRepository.save(userWallet);

        return CreateWalletResponseDTO.of(
                userWallet.getWalletId(),
                userWallet.getName(),
                userWallet.getAddress(),
                userWallet.getBlockchain(),
                userWallet.getStatus(),
                userWallet.getCreatedAt(),
                userWallet.getUpdatedAt()
        );
    }

    @Transactional
    public TransferResponseDTO transfer(TransferRequestDTO request) {

        // 보내는 사용자 지갑 조회
        UserWallet userWallet = userWalletRepository.findByWalletId(request.getUserWalletId())
                .orElseThrow(()-> new IllegalArgumentException(String.format("Can Not Found User wallet %s", request.getUserWalletId())));

        // 해당 사용자 지갑이 속한 마스터지갑 잔고 조회
        Amount spendableAmount = exchange.getMasterWalletBalance(request.getTicker());


        // 출금 가능 여부 판단
        Amount walletBalance = userWallet.getBalance();
        Amount amount = request.getAmount();
        if (spendableAmount.compareTo(walletBalance) < 0
                && spendableAmount.compareTo(request.getAmount()) < 0) {
            throw new IllegalStateException("Not Enough Money..!");
        }

        // 마스터 지갑에서 코인/토큰 전송하기 API 호출
        Transaction transaction = exchange.transfer(
                request.getAmount(),
                request.getTo(),
                request.getTicker()
        );

        // 사용자 지갑 잔고 차감
        walletBalance.subtract(amount);
        userWalletRepository.updateWalletBalance(walletBalance, request.getUserWalletId());


        return TransferResponseDTO.of(
                transaction.getWalletName(),
                request.getAmount(),
                walletBalance
        );
    }

    @Transactional
    public FlushResponseDTO flush(FlushRequestDTO request) {

        List<String> userWalletIds = exchange.getUserWalletIds();

        Transaction transaction = exchange.flushAll(
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


    /**
     *  테스트용 모든 지갑 채워넣기
     */

    @Transactional
    public void updateWalletList() {
        List<UserWallet> userWallets = exchange.getAllUserWallet().stream()
                .filter(u->!userWalletRepository.existsUserWalletByWalletIdAndStatus(u.getWalletId(), u.getStatus()))

                .collect(Collectors.toList());
        userWalletRepository.saveAll(userWallets);
    }

    @Transactional
    public void updateTransactionList() {
        List<Transaction> transactions = exchange.getValueTransferEvents("").stream()
                .filter(t->!transactionRepository.existsTransactionByDetailId(t.getDetailId()))
                .collect(Collectors.toList());

        transactionRepository.saveAll(transactions);
    }
}
