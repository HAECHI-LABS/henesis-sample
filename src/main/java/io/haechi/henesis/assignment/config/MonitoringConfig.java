package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.Situation;
import io.haechi.henesis.assignment.domain.UpdateAction;
import io.haechi.henesis.assignment.domain.UpdateBalanceAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MonitoringConfig {

    @Bean
    public ActionSupplier<UpdateAction> actionSupplier(
            UpdateBalanceAction updateBalanceAction
    ) {
        Map<Situation, UpdateAction> s = new HashMap<>();
        s.put(Situation.DEPOSIT_ETH_KLAY, updateBalanceAction);
        s.put(Situation.ROLLBACK_ETH_KLAY, updateBalanceAction);
        s.put(Situation.DEPOSIT_BTC, updateBalanceAction);
        s.put(Situation.ROLLBACK_BTC, updateBalanceAction);

        s.put(Situation.NOTHING_TO_DO, null);

        return new ActionSupplier<>(s);
    }

}
