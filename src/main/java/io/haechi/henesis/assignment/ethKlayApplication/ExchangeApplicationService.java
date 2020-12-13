package io.haechi.henesis.assignment.ethKlayApplication;

import io.haechi.henesis.assignment.ethKlayApplication.dto.*;
import io.haechi.henesis.assignment.ethKlayDomain.*;
import io.haechi.henesis.assignment.ethKlayDomain.transaction.Transaction;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class ExchangeApplicationService {

    private final EthKlayHenesisWalletClient ethKlayHenesisWalletClient;
    private final WalletRepository walletRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;

    public ExchangeApplicationService(EthKlayHenesisWalletClient ethKlayHenesisWalletClient,
                                      WalletRepository walletRepository,
                                      FlushedTransactionRepository flushedTransactionRepository) {
        this.ethKlayHenesisWalletClient = ethKlayHenesisWalletClient;
        this.walletRepository = walletRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
    }


    @Transactional
    public CreateWalletResponseDTO createUserWallet(CreateWalletRequestDTO request) {

        Wallet wallet = ethKlayHenesisWalletClient.createUserWallet(request.getWalletName());
        walletRepository.save(wallet);
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
        Wallet wallet = walletRepository.findByWalletId(request.getUserWalletId())
                .orElseThrow(()-> new IllegalArgumentException(String.format("Can Not Found User wallet %s", request.getUserWalletId())));


        // 사용자 지갑 상태가 ACTIVE 가 아닌 경우 에러 발생
        if (!wallet.getStatus().contains("ACTIVE")){
            throw new IllegalStateException("User Wallet is NOT ACTIVE Status");
        }

        // 해당 사용자 지갑이 속한 마스터지갑 잔고 조회
        Amount spendableAmount = ethKlayHenesisWalletClient.getMasterWalletBalance();
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
                request.getTo()
        );

        // 사용자 지갑 잔고 차감
        balance.subtract(amount);
        walletRepository.save(wallet);
        log.info(String.format("UserWallet(%s) Balance : %s",wallet.getName(),balance.toHexString()));


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
                userWalletIds
        );

        flushedTransactionRepository.save(
                FlushedTransaction.of(
                    transaction.getTransactionId(),
                    transaction.getBlockchain(),
                    transaction.getStatus(),
                    transaction.getCreatedAt(),
                    transaction.getUpdatedAt()
                )
        );
        log.info(String.format("%s Flush..!",transaction.getStatus()));

        return FlushResponseDTO.of(
                transaction.getTransactionId(),
                transaction.getBlockchain(),
                transaction.getStatus()
        );
    }




    /**
     *  테스트용 모든 지갑, 트랜잭션 채워넣기 (싱크 맞출 때 사용해도 좋을 듯?)
     */

    @Transactional
    public void updateWalletList() {
        List<Wallet> wallets = ethKlayHenesisWalletClient.getAllUserWallet().stream()
                .filter(u->!walletRepository.existsUserWalletByWalletIdAndStatus(u.getWalletId(), u.getStatus()))

                .collect(Collectors.toList());
        walletRepository.saveAll(wallets);

        List<Wallet> masterWallets = ethKlayHenesisWalletClient.getAllMasterWallet().stream()
                .filter(m->!walletRepository.existsUserWalletByWalletIdAndStatus(m.getWalletId(), m.getStatus()))
                .collect(Collectors.toList());

        walletRepository.saveAll(masterWallets);

    }
}
