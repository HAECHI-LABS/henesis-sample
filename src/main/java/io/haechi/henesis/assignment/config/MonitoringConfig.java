package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.domain.transaction.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MonitoringConfig {

    @Bean
    public ActionSupplier<Action> actionSupplier(
            UpdateBalanceAction updateBalanceAction,
            UpdateStatusAction updateStatusAction
    ) {
        Map<Situation, Action> s = new HashMap<>();
        s.put(Situation.DEPOSIT_CONFIRMED, updateBalanceAction);
        s.put(Situation.ROLLBACK, updateBalanceAction);
        s.put(Situation.UPDATE_STATUS, updateStatusAction);

        return new ActionSupplier<>(s);
    }

}
