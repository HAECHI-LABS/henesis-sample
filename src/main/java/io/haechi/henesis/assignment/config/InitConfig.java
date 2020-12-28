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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class InitConfig implements CommandLineRunner {
    private HenesisClient klayHenesisClient;
    private HenesisClient ethHenesisClient;
    private HenesisClient btcHenesisClient;
    private DepositAddressRepository depositAddressRepository;
    private BalanceRepository balanceRepository;
    private TransferRepository transferRepository;

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
        Pageable pageable = PageRequest.of(0, 50);
        while(true) {
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

        pageable = PageRequest.of(0, 50);
        while(true) {
            Pagination<Transfer> transferPagination = client.getTransfersByUpdatedAtGte(LocalDateTime.of(0,1,1,0, 0), pageable);

            if (transferPagination.getResults().isEmpty()) {
                break;
            }

            transferPagination.getResults().forEach(transfer -> {
                if (!this.transferRepository.existsByHenesisId(transfer.getHenesisId())) {
                    this.transferRepository.save(transfer);
                }
            });

            pageable = pageable.next();
        }

        // henesis dosen't manage deposit address's balance for Bitcoin
        if(blockchain.equals(Blockchain.BITCOIN)) {
            return;
        }

        List<DepositAddress> depositAddresses = this.depositAddressRepository.findAllByBlockchain(blockchain);
        depositAddresses.forEach(depositAddress -> {
            List<Balance> onchainBalances = client.getDepositAddressBalances(depositAddress);
            onchainBalances.forEach(onchainBalance -> {
                Balance offchainBalance = this.balanceRepository.findByDepositAddressAndSymbol(depositAddress, onchainBalance.getSymbol())
                        .orElse(Balance.zero(depositAddress, onchainBalance.getSymbol()));

                if (onchainBalance.getAmount().compareTo(offchainBalance.getAmount()) != 0) {
                    this.balanceRepository.save(onchainBalance);
                }
            });
        });
    }
}
