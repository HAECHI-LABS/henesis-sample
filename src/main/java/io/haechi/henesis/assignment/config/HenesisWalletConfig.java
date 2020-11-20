package io.haechi.henesis.assignment.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({HenesisWalletProperties.class})
public class HenesisWalletConfig {

    @Bean
    public HenesisWalletProperties henesisWalletProperties(){return new HenesisWalletProperties();}

    @Bean
    @Qualifier("walletUrl")
    public String url(HenesisWalletProperties henesisWalletProperties){
        return henesisWalletProperties.getUrl();
    }

    @Bean
    @Qualifier("walletApiSecret")
    public String apiSecret(HenesisWalletProperties henesisWalletProperties){
        return henesisWalletProperties.getApiSecret();
    }

    @Bean
    @Qualifier("walletAccessToken")
    public String accessToken(HenesisWalletProperties henesisWalletProperties){
        return henesisWalletProperties.getAccessToken();
    }

    @Bean
    @Qualifier("masterWalletId")
    public String masterWalletId(HenesisWalletProperties henesisWalletProperties){
        return henesisWalletProperties.getMasterWalletId();
    }

    @Bean
    @Qualifier("walletPassphrase")
    public String walletPassphrase(HenesisWalletProperties henesisWalletProperties){
        return henesisWalletProperties.getPassphrase();
    }
}
