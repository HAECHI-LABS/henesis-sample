package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.CreateDepositAddressRequest;
import io.haechi.henesis.assignment.application.dto.CreateDepositAddressResponse;
import io.haechi.henesis.assignment.application.dto.FlushResponse;
import io.haechi.henesis.assignment.application.dto.TransferRequest;
import io.haechi.henesis.assignment.application.dto.TransferResponse;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.EthKlayWalletService;
import io.haechi.henesis.assignment.domain.HenesisClientSupplier;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.TransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class ExchangeApplicationService {
    private final HenesisClientSupplier henesisClientSupplier;
    private final DepositAddressRepository depositAddressRepository;
    private final TransferRepository transferRepository;

    public ExchangeApplicationService(
            HenesisClientSupplier henesisClientSupplier,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository
    ) {
        this.henesisClientSupplier = henesisClientSupplier;
        this.depositAddressRepository = depositAddressRepository;
        this.transferRepository = transferRepository;
    }


    @Transactional
    public CreateDepositAddressResponse createDepositAddress(Blockchain blockchain, CreateDepositAddressRequest request) {
        DepositAddress depositAddress = this.henesisClientSupplier.supply(blockchain).createDepositAddress(request.getName());
        this.depositAddressRepository.save(depositAddress);

        log.info(String.format("Creating User Wallet (%s)", request.getName()));
        return CreateDepositAddressResponse.of(request.getName());
    }

    @Transactional
    public TransferResponse transfer(
            Long depositAddressId,
            Blockchain blockchain,
            TransferRequest request
    ) {

        // 보내는 사용자 지갑 조회 for check and update User Wallet Balance
        DepositAddress depositAddress = this.depositAddressRepository.findById(depositAddressId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("ERROR : Can Not Found User Wallet. (%s)", depositAddressId)));

        // TODO: spendable balance 확인
        // TODO: REQUIRED_NEW
        Transfer transfer = this.transferRepository.save(
                Transfer.ready(
                        depositAddress.getAddress(),
                        request.getTo(),
                        request.getAmount(),
                        request.getSymbol(),
                        blockchain,
                        depositAddress.getId()
                )
        );
//        depositAddress.withdrawBy(
//                request.getAmount(),
//                ethKlayWalletService.getMasterWalletBalance()
//        );
        /* BTC
              depositAddress.withdrawBy(
                request.getAmount(),
                this.henesisClient.getEstimatedFee(),
                this.henesisClient.getMasterWalletBalance()
        );
         */
//
//        this.depositAddressRepository.save(depositAddress);
        log.info(String.format("Withdraw Balance..! (%s)", depositAddress.getName()));

        transfer.syncWithHenesis(
                this.henesisClientSupplier.supply(blockchain).transfer(transfer.getTo(), transfer.getAmount())
        );
        log.info(String.format("Transfer Requested..! (%s)", depositAddress.getName()));

        return TransferResponse.of(
                depositAddress.getName(),
                request.getAmount().toHexString()
        );
    }

    @Transactional
    public FlushResponse flush(Blockchain blockchain) {
        return null;
//        List<String> userWalletIds = ethKlayWalletService.getDepositAddressIds();
//
//        Transaction transaction = ethKlayWalletService.flushAll(userWalletIds);
//        flushedTransactionRepository.save(
//                FlushedTransaction.of(
//                        transaction.getTransactionId(),
//                        transaction.getBlockchain(),
//                        transaction.getStatus(),
//                        transaction.getCreatedAt(),
//                        transaction.getUpdatedAt()
//                )
//        );
//        log.info(String.format("Flush Requested..! (%s)", transaction.getBlockchain()));
//
//        return FlushResponse.of(
//                transaction.getTransactionId(),
//                transaction.getBlockchain(),
//                transaction.getStatus()
//        );

    }
}
