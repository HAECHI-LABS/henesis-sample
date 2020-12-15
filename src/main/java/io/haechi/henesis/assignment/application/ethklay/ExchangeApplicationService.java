package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.application.ethklay.dto.CreateWalletRequest;
import io.haechi.henesis.assignment.application.ethklay.dto.FlushRequest;
import io.haechi.henesis.assignment.application.ethklay.dto.TransferRequest;
import io.haechi.henesis.assignment.application.ethklay.dto.TransferResponse;
import io.haechi.henesis.assignment.domain.Transaction;
import io.haechi.henesis.assignment.domain.ethklay.*;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class ExchangeApplicationService {

    private final EthKlayWalletService ethKlayWalletService;
    private final WalletRepository walletRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;

    public ExchangeApplicationService(EthKlayWalletService ethKlayWalletService,
                                      WalletRepository walletRepository,
                                      FlushedTransactionRepository flushedTransactionRepository) {
        this.ethKlayWalletService = ethKlayWalletService;
        this.walletRepository = walletRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
    }


    @Transactional
    public void createUserWallet(CreateWalletRequest request) {
        try {
            Wallet wallet = ethKlayWalletService.createUserWallet(request.getWalletName());
            walletRepository.save(wallet);
        } catch (Exception e) {
            log.info("ERROR : Can Not Create User Wallet.");
        }

        log.info(String.format("Creating User Wallet (%s)", request.getWalletName()));
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        // 보내는 사용자 지갑 조회 for check and update User Wallet Balance
        Wallet wallet = walletRepository.findByWalletId(request.getUserWalletId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can Not Found User Wallet %s", request.getUserWalletId())));

        // 사용자 지갑 상태가 ACTIVE 가 아닌 경우 에러 발생
        if (!wallet.getStatus().equals("ACTIVE")) {
            throw new IllegalStateException("User Wallet Is NOT ACTIVE Status");
        }

        // 해당 사용자 지갑이 속한 마스터지갑 잔고 조회
        Amount spendableAmount = ethKlayWalletService.getMasterWalletBalance();
        Amount balance = wallet.getBalance();
        Amount amount = request.getAmount();

        // 사용자 지갑 잔고 차감
        balance.withdrawBy(amount, balance, spendableAmount);

        try {
            walletRepository.save(wallet);
            ethKlayWalletService.transfer(request.getAmount(), request.getTo());
        } catch (Exception e) {
            log.info("ERROR : Can Not Transfer");
        }

        log.info(String.format("User Wallet(%s) Balance : %s", wallet.getName(), balance.toHexString()));


        return TransferResponse.of(
                wallet.getName(),
                request.getAmount(),
                balance
        );
    }

    @Transactional
    public void flush(FlushRequest request) {

        List<String> userWalletIds = ethKlayWalletService.getUserWalletIds();
        try {
            Transaction transaction = ethKlayWalletService.flushAll(userWalletIds);
            flushedTransactionRepository.save(
                    FlushedTransaction.of(
                            transaction.getTransactionId(),
                            transaction.getBlockchain(),
                            transaction.getStatus(),
                            transaction.getCreatedAt(),
                            transaction.getUpdatedAt()
                    )
            );

        } catch (Exception e) {
            log.info("ERROR : Can Not Flush");
        }

        log.info("Flush Requested..!");
    }


    /**
     * 테스트용 모든 지갑, 트랜잭션 채워넣기 (싱크 맞출 때 사용해도 좋을 듯?)
     */

    @Transactional
    public void updateWalletList() {
        List<Wallet> wallets = ethKlayWalletService.getAllUserWallet().stream()
                .filter(u -> !walletRepository.existsUserWalletByWalletIdAndStatus(u.getWalletId(), u.getStatus()))

                .collect(Collectors.toList());
        walletRepository.saveAll(wallets);

        List<Wallet> masterWallets = ethKlayWalletService.getAllMasterWallet().stream()
                .filter(m -> !walletRepository.existsUserWalletByWalletIdAndStatus(m.getWalletId(), m.getStatus()))
                .collect(Collectors.toList());

        walletRepository.saveAll(masterWallets);

    }
}
