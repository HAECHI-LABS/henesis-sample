package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.domain.BalanceManager;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.TransferRepository;
import io.haechi.henesis.assignment.scheduler.BtcMonitoringScheduler;
import io.haechi.henesis.assignment.scheduler.MonitoringScheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringConfig {

    @Bean
    public MonitoringScheduler ethMonitoringScheduler(
            @Qualifier("ethHenesisClient") HenesisClient ethHenesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository,
            BalanceManager balanceManager,
            HenesisProperties henesisProperties
    ) {
        return new MonitoringScheduler(
                ethHenesisClient,
                depositAddressRepository,
                transferRepository,
                balanceManager,
                Blockchain.ETHEREUM,
                henesisProperties.getEthPollingSize()
        );
    }

    @Bean
    public MonitoringScheduler klayMonitoringScheduler(
            @Qualifier("klayHenesisClient") HenesisClient klayHenesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository,
            BalanceManager balanceManager,
            HenesisProperties henesisProperties
    ) {
        return new MonitoringScheduler(
                klayHenesisClient,
                depositAddressRepository,
                transferRepository,
                balanceManager,
                Blockchain.KLAYTN,
                henesisProperties.getKlayPollingSize()
        );
    }

    @Bean
    public BtcMonitoringScheduler btcMonitoringScheduler(
            @Qualifier("btcHenesisClient") HenesisClient btcHenesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository,
            BalanceManager balanceManager,
            HenesisProperties henesisProperties
    ) {
        return new BtcMonitoringScheduler(
                btcHenesisClient,
                depositAddressRepository,
                transferRepository,
                balanceManager,
                henesisProperties.getBtcPollingSize()
        );
    }
}
