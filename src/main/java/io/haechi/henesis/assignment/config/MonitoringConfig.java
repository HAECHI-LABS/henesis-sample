package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.scheduler.BtcMonitoringScheduler;
import io.haechi.henesis.assignment.scheduler.MonitoringScheduler;
import io.haechi.henesis.assignment.domain.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MonitoringConfig {

    @Bean
    public MonitoringScheduler ethMonitoringScheduler(
            @Qualifier("ethHenesisClient") HenesisClient ethHenesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository
    ) {
        return new MonitoringScheduler(
                ethHenesisClient,
                depositAddressRepository,
                transferRepository,
                Blockchain.KLAYTN
        );
    }

    @Bean
    public MonitoringScheduler klayMonitoringScheduler(
            @Qualifier("klayHenesisClient") HenesisClient klayHenesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository
    ) {
        return new MonitoringScheduler(
                klayHenesisClient,
                depositAddressRepository,
                transferRepository,
                Blockchain.ETHEREUM
        );
    }

    @Bean
    public BtcMonitoringScheduler btcMonitoringScheduler(
            @Qualifier("btcHenesisClient") HenesisClient btcHenesisClient,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository
    ) {
        return new BtcMonitoringScheduler(
                btcHenesisClient,
                depositAddressRepository,
                transferRepository
        );
    }

    @Bean
    public ActionSupplier<UpdateAction> actionSupplier(
            UpdateBalanceAction updateBalanceAction,
            UpdateStatusAction updateStatusAction
    ) {
        Map<Situation, UpdateAction> s = new HashMap<>();
        s.put(Situation.DEPOSIT_ETH_KLAY, updateBalanceAction);
        s.put(Situation.ROLLBACK_ETH_KLAY, updateBalanceAction);
        s.put(Situation.DEPOSIT_BTC, updateBalanceAction);
        s.put(Situation.ROLLBACK_BTC, updateBalanceAction);

        s.put(Situation.NOTHING_TO_DO, updateStatusAction);

        return new ActionSupplier<>(s);
    }

}
