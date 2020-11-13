package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.domain.Balance;
import io.haechi.henesis.assignment.domain.BalanceRepository;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.Pagination;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.TransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile("init")
@Slf4j
public class InitConfig implements CommandLineRunner {
    private final HenesisClient klayHenesisClient;
    private final HenesisClient ethHenesisClient;
    private final HenesisClient btcHenesisClient;
    private final DepositAddressRepository depositAddressRepository;
    private final BalanceRepository balanceRepository;
    private final TransferRepository transferRepository;

    public InitConfig(
            @Qualifier("klayHenesisClient") HenesisClient klayHenesisClient,
            @Qualifier("ethHenesisClient") HenesisClient ethHenesisClient,
            @Qualifier("btcHenesisClient") HenesisClient btcHenesisClient,
            DepositAddressRepository depositAddressRepository,
            BalanceRepository balanceRepository,
            TransferRepository transferRepository
    ) {
        this.klayHenesisClient = klayHenesisClient;
        this.ethHenesisClient = ethHenesisClient;
        this.btcHenesisClient = btcHenesisClient;
        this.depositAddressRepository = depositAddressRepository;
        this.balanceRepository = balanceRepository;
        this.transferRepository = transferRepository;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        this.sync(this.klayHenesisClient, Blockchain.KLAYTN);
        this.sync(this.ethHenesisClient, Blockchain.ETHEREUM);
        this.sync(this.btcHenesisClient, Blockchain.BITCOIN);
    }

    private void sync(HenesisClient client, Blockchain blockchain) {
        log.info(String.format("start to init '%s' deposit addresses", blockchain.toString()));
        Pageable pageable = PageRequest.of(0, 50);
        while (true) {
            Pagination<DepositAddress> depositAddressPagination = client.getDepositAddresses(pageable);

            if (depositAddressPagination.getResults().isEmpty()) {
                break;
            }

            depositAddressPagination.getResults().forEach(depositAddress -> {
                if (!this.depositAddressRepository.existsByHenesisId(depositAddress.getHenesisId())) {
                    this.depositAddressRepository.save(depositAddress);
                }
            });

            pageable = pageable.next();
        }

        log.info(String.format("end to init '%s' deposit addresses", blockchain.toString()));
        log.info(String.format("start to init '%s' transfers", blockchain.toString()));
        pageable = PageRequest.of(0, 50);
        while (true) {
            Pagination<Transfer> transferPagination = client.getTransfersByUpdatedAtGte(LocalDateTime.of(0, 1, 1, 0, 0), pageable);

            if (transferPagination.getResults().isEmpty()) {
                break;
            }

            transferPagination.getResults().forEach(transfer -> {
                if (!this.transferRepository.existsByHenesisTransferId(transfer.getHenesisTransferId())) {
                    this.transferRepository.save(transfer);
                }
            });

            pageable = pageable.next();
        }

        log.info(String.format("end to init '%s' transfers", blockchain.toString()));

        // henesis dosen't manage deposit address's balance for Bitcoin
        if (blockchain.equals(Blockchain.BITCOIN)) {
            return;
        }

        log.info(String.format("start to init '%s' balances", blockchain.toString()));
        List<DepositAddress> depositAddresses = this.depositAddressRepository.findAllByBlockchain(blockchain);
        depositAddresses.forEach(depositAddress -> {
            List<Balance> onchainBalances = client.getDepositAddressBalances(depositAddress);
            onchainBalances.forEach(onchainBalance -> {
                Balance offchainBalance = this.balanceRepository.findByDepositAddressAndSymbol(depositAddress, onchainBalance.getSymbol().toUpperCase())
                        .orElse(Balance.zero(depositAddress, onchainBalance.getSymbol().toUpperCase()));

                if (onchainBalance.getAmount().compareTo(offchainBalance.getAmount()) != 0) {
                    offchainBalance.changeAmount(onchainBalance.getAmount());
                    this.balanceRepository.save(offchainBalance);
                }
            });
        });

        log.info(String.format("end to init '%s' balances", blockchain.toString()));
    }
}
