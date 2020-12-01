package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.domain.transaction.Action;
import io.haechi.henesis.assignment.domain.transaction.ActionSupplier;
import io.haechi.henesis.assignment.domain.transaction.Situation;
import io.haechi.henesis.assignment.domain.transaction.UpdateBalanceAction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({HenesisWalletProperties.class})
public class HenesisWalletConfig {

    @Bean
    public HenesisWalletProperties henesisWalletProperties() {
        return new HenesisWalletProperties();
    }

    @Bean
    @Qualifier("walletUrl")
    public String url(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getUrl();
    }

    @Bean
    @Qualifier("walletApiSecret")
    public String apiSecret(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getApiSecret();
    }

    @Bean
    @Qualifier("walletAccessToken")
    public String accessToken(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getAccessToken();
    }

    @Bean
    @Qualifier("masterWalletId")
    public String masterWalletId(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getMasterWalletId();
    }

    @Bean
    @Qualifier("passphrase")
    public String passphrase(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getPassphrase();
    }

}
