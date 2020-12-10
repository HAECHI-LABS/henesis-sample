package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.*;
import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.transaction.Transaction;
import io.haechi.henesis.assignment.domain.transaction.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExchangeApplicationService {

    private final EthKlayHenesisWalletClient ethKlayHenesisWalletClient;
    private final UserWalletRepository userWalletRepository;
    private final TransactionRepository transactionRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;

    public ExchangeApplicationService(EthKlayHenesisWalletClient ethKlayHenesisWalletClient,
                                      UserWalletRepository userWalletRepository,
                                      TransactionRepository transactionRepository,
                                      FlushedTransactionRepository flushedTransactionRepository) {
        this.ethKlayHenesisWalletClient = ethKlayHenesisWalletClient;
        this.userWalletRepository = userWalletRepository;
        this.transactionRepository =transactionRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
    }


    @Transactional
    public CreateWalletResponseDTO createUserWallet(CreateWalletRequestDTO request) {

        Wallet wallet = ethKlayHenesisWalletClient.createUserWallet(request.getWalletName());
        userWalletRepository.save(wallet);
        log.info(String.format("Creating UserWallet (%s)",request.getWalletName()));
        return CreateWalletResponseDTO.of(
                wallet.getWalletId(),
                wallet.getName(),
                wallet.getAddress(),
                wallet.getBlockchain(),
                wallet.getStatus(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );
    }

    @Transactional
    public TransferResponseDTO transfer(TransferRequestDTO request) {

        // 보내는 사용자 지갑 조회 for check and update User Wallet Balance
        Wallet wallet = userWalletRepository.findByWalletId(request.getUserWalletId())
                .orElseThrow(()-> new IllegalArgumentException(String.format("Can Not Found User wallet %s", request.getUserWalletId())));

        // 사용자 지갑 상태가 ACTIVE 가 아닌 경우 에러 발생
        if (!wallet.getStatus().contains("ACTIVE")){
            throw new IllegalStateException("User Wallet is NOT ACTIVE Status");
        }

        // 해당 사용자 지갑이 속한 마스터지갑 잔고 조회
        Amount spendableAmount = ethKlayHenesisWalletClient.getMasterWalletBalance(request.getTicker());
        Amount balance = wallet.getBalance();
        Amount amount = request.getAmount();

        // 출금 가능 여부 판단
        if (balance.compareTo(request.getAmount()) < 0) {
            throw new IllegalStateException("Not Enough User Wallet Balance..!");
        }
        if (spendableAmount.compareTo(balance)<0){
            throw new IllegalStateException("Not Enough Master Wallet Balance..!");
        }


        // 마스터 지갑에서 코인/토큰 전송하기 API 호출
        Transaction transaction = ethKlayHenesisWalletClient.transfer(
                request.getAmount(),
                request.getTo(),
                request.getTicker()
        );

        // 사용자 지갑 잔고 차감
        balance.subtract(amount);
        userWalletRepository.updateWalletBalance(balance, request.getUserWalletId());
        log.info(String.format("UserWallet(%s) Balance : %f",wallet.getName(),balance.toDouble()));


        return TransferResponseDTO.of(
                transaction.getWalletName(),
                request.getAmount(),
                balance
        );
    }

    @Transactional
    public FlushResponseDTO flush(FlushRequestDTO request) {

        List<String> userWalletIds = ethKlayHenesisWalletClient.getUserWalletIds();

        Transaction transaction = ethKlayHenesisWalletClient.flushAll(
                request.getTicker(),
                userWalletIds
        );

        flushedTransactionRepository.save(
                FlushedTransaction.of(
                    transaction.getTransactionId(),
                    transaction.getBlockchain(),
                    transaction.getStatus(),
                    transaction.getCreatedAt()
                )
        );
        log.info(String.format("%s Flush..!",transaction.getStatus()));

        return FlushResponseDTO.builder()
                .txId(transaction.getTransactionId())
                .blockchain(transaction.getBlockchain())
                .status(transaction.getStatus())
                .build();
    }




    /**
     *  테스트용 모든 지갑, 트랜잭션 채워넣기 (싱크 맞출 때 사용해도 좋을 듯?)
     */

    @Transactional
    public void updateWalletList() {
        List<Wallet> wallets = ethKlayHenesisWalletClient.getAllUserWallet().stream()
                .filter(u->!userWalletRepository.existsUserWalletByWalletIdAndStatus(u.getWalletId(), u.getStatus()))

                .collect(Collectors.toList());
        userWalletRepository.saveAll(wallets);

        List<Wallet> masterWallets = ethKlayHenesisWalletClient.getAllMasterWallet().stream()
                .filter(m->!userWalletRepository.existsUserWalletByWalletIdAndStatus(m.getWalletId(), m.getStatus()))
                .collect(Collectors.toList());

        userWalletRepository.saveAll(masterWallets);

    }

    @Transactional
    public void updateTransactionList() {
        List<Transaction> transactions = ethKlayHenesisWalletClient.getValueTransferEvents("").stream()
                .filter(t-> transactionRepository.findTransactionByDetailId(t.getDetailId()).isPresent())
                .collect(Collectors.toList());

        transactionRepository.saveAll(transactions);
    }
}
