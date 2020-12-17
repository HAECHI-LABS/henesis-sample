package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.application.ethklay.dto.*;
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
    public CreateWalletResponse createUserWallet(CreateWalletRequest request) {
        Wallet wallet = ethKlayWalletService.createUserWallet(request.getName());
        walletRepository.save(wallet);

        log.info(String.format("Creating User Wallet (%s)", request.getName()));
        return CreateWalletResponse.of(request.getName());
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        // 보내는 사용자 지갑 조회 for check and update User Wallet Balance
        Wallet wallet = walletRepository.findByWalletId(request.getUserWalletId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can Not Found User Wallet %s", request.getUserWalletId())));

        wallet.withdrawBy(
                request.getAmount(),
                ethKlayWalletService.getMasterWalletBalance()
        );
        walletRepository.save(wallet);
        log.info(String.format("Update Balance (%s)", wallet.getName()));

        ethKlayWalletService.transfer(request.getAmount(), request.getTo());
        log.info(String.format("Transfer Requested..! (%s)", wallet.getName()));

        return TransferResponse.of(
                wallet.getName(),
                request.getAmount().toHexString()
        );
    }

    @Transactional
    public FlushResponse flush() {

        List<String> userWalletIds = ethKlayWalletService.getUserWalletIds();

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
        log.info(String.format("Flush Requested..! (%s)", transaction.getBlockchain()));

        return FlushResponse.of(
                transaction.getTransactionId(),
                transaction.getBlockchain(),
                transaction.getStatus()
        );

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
