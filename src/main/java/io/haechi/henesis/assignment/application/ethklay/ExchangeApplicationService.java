package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.application.ethklay.dto.*;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.ethklay.*;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
public abstract class ExchangeApplicationService {

    private final EthKlayWalletService ethKlayWalletService;
    private final DepositAddressRepository depositAddressRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;

    public ExchangeApplicationService(EthKlayWalletService ethKlayWalletService,
                                      DepositAddressRepository depositAddressRepository,
                                      FlushedTransactionRepository flushedTransactionRepository) {
        this.ethKlayWalletService = ethKlayWalletService;
        this.depositAddressRepository = depositAddressRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
    }


    @Transactional
    public CreateWalletResponse createUserWallet(CreateWalletRequest request) {
        DepositAddress depositAddress = ethKlayWalletService.createDepositAddress(request.getName());
        this.depositAddressRepository.save(depositAddress);

        log.info(String.format("Creating User Wallet (%s)", request.getName()));
        return CreateWalletResponse.of(request.getName());
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        // 보내는 사용자 지갑 조회 for check and update User Wallet Balance
        DepositAddress depositAddress = this.depositAddressRepository.findByAddress(request.getFrom())
                .orElseThrow(() -> new IllegalArgumentException(String.format("ERROR : Can Not Found User Wallet. (%s)", request.getFrom())));

        depositAddress.withdrawBy(
                request.getAmount(),
                ethKlayWalletService.getMasterWalletBalance()
        );

        this.depositAddressRepository.save(depositAddress);
        log.info(String.format("Withdraw Balance..! (%s)", depositAddress.getName()));

        ethKlayWalletService.transfer(request.getAmount(), request.getTo());
        log.info(String.format("Transfer Requested..! (%s)", depositAddress.getName()));

        return TransferResponse.of(
                depositAddress.getName(),
                request.getAmount().toHexString()
        );
    }

    @Transactional
    public FlushResponse flush() {

        List<String> userWalletIds = ethKlayWalletService.getDepositAddressIds();

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
}
