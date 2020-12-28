package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.CreateDepositAddressRequest;
import io.haechi.henesis.assignment.application.dto.CreateDepositAddressResponse;
import io.haechi.henesis.assignment.application.dto.FlushResponse;
import io.haechi.henesis.assignment.application.dto.TransferRequest;
import io.haechi.henesis.assignment.application.dto.TransferResponse;
import io.haechi.henesis.assignment.domain.BalanceManager;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.HenesisClientSupplier;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.TransferRepository;
import io.haechi.henesis.assignment.domain.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExchangeApplicationService {
    private final HenesisClientSupplier henesisClientSupplier;
    private final DepositAddressRepository depositAddressRepository;
    private final TransferRepository transferRepository;
    private final BalanceManager balanceManager;

    public ExchangeApplicationService(
            HenesisClientSupplier henesisClientSupplier,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository,
            BalanceManager balanceManager
    ) {
        this.henesisClientSupplier = henesisClientSupplier;
        this.depositAddressRepository = depositAddressRepository;
        this.transferRepository = transferRepository;
        this.balanceManager = balanceManager;
    }

    @Transactional
    public CreateDepositAddressResponse createDepositAddress(Blockchain blockchain, CreateDepositAddressRequest request) {
        DepositAddress depositAddress = this.henesisClientSupplier.supply(blockchain).createDepositAddress(request.getName());
        log.info(String.format("Creating User Wallet (%s)", request.getName()));
        return CreateDepositAddressResponse.of(this.depositAddressRepository.save(depositAddress));
    }

    // Advanced: when fail to save transfer
    @Transactional
    public TransferResponse transfer(
            Long depositAddressId,
            Blockchain blockchain,
            TransferRequest request
    ) {
        DepositAddress depositAddress = this.depositAddressRepository.findById(depositAddressId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("ERROR : Can Not Found User Wallet. (%s)", depositAddressId)));

        Transfer transfer = depositAddress.transfer(
                request.getTo(),
                request.getAmount(),
                request.getSymbol(),
                this.henesisClientSupplier.supply(blockchain),
                this.balanceManager
        );

        log.info(String.format("Transfer Requested..! (%s)", depositAddress.getName()));

        return new TransferResponse(this.transferRepository.save(transfer));
    }

    @Transactional
    public FlushResponse flush(
            Blockchain blockchain,
            List<Long> depositAddressIds,
            String symbol
    ) {
        HenesisClient henesisClient = this.henesisClientSupplier.supply(blockchain);
        try {
            henesisClient.getCoin(symbol);
        } catch (Exception e) {
            throw new BadRequestException(String.format("henesis doesn't support '%s'", symbol));
        }

        if (blockchain.equals(Blockchain.BITCOIN)) {
            throw new IllegalArgumentException("btc doesn't support flush");
        }

        if (depositAddressIds.size() >= 50) {
            throw new IllegalArgumentException("flush is possible only for 50 at a time");
        }

        if (depositAddressIds == null || depositAddressIds.isEmpty()) {
            throw new IllegalArgumentException("depositAddressIds is null or empty");
        }

        List<DepositAddress> depositAddresses = this.depositAddressRepository.findAllByBlockchainAndIdIn(blockchain, depositAddressIds);
        if (depositAddressIds.size() != depositAddresses.size()) {
            depositAddressIds.remove(
                    depositAddresses.stream()
                            .map(DepositAddress::getId)
                            .collect(Collectors.toList())
            );

            throw new BadRequestException(
                    String.format(
                            "there is no matched deposit address id with '%s'",
                            depositAddressIds.stream()
                                    .map(Object::toString)
                                    .collect(Collectors.joining(", "))
                    )
            );
        }
        Transfer flushTransfer = henesisClient.flush(
                depositAddresses.stream()
                        .map(DepositAddress::getHenesisId)
                        .collect(Collectors.toList())
        );

        log.info(String.format("Flush Requested..! (%s)", flushTransfer.getBlockchain()));

        return new FlushResponse(this.transferRepository.save(flushTransfer));
    }
}
