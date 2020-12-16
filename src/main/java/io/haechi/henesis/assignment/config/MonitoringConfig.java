package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MonitoringConfig {

    @Bean
    public ActionSupplier<UpdateAction> actionSupplier(
            BalanceUpdateAction balanceUpdater,
            StatusUpdateAction txStatusUpdater
    ) {
        Map<Situation, UpdateAction> s = new HashMap<>();
        s.put(Situation.DEPOSIT_CONFIRMED, balanceUpdater);
        s.put(Situation.ROLLBACK, balanceUpdater);
        s.put(Situation.NOTHING_TO_DO, txStatusUpdater);

        return new ActionSupplier<>(s);
    }

}
