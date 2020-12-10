package io.haechi.henesis.assignment.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    @Qualifier("ethMasterWalletId")
    public String ethMasterWalletId(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getEthMasterWalletId();
    }
    @Bean
    @Qualifier("klayMasterWalletId")
    public String klayMasterWalletId(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getKlayMasterWalletId();
    }
    @Bean
    @Qualifier("btcWalletId")
    public String btcWalletId(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getBtcWalletId();
    }

    @Bean
    @Qualifier("ethPassphrase")
    public String ethPassphrase(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getEthPassphrase();
    }
    @Bean
    @Qualifier("klayPassphrase")
    public String klayPassphrase(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getKlayPassphrase();
    }
    @Bean
    @Qualifier("btcPassphrase")
    public String btcPassphrase(HenesisWalletProperties henesisWalletProperties) {
        return henesisWalletProperties.getBtcPassphrase();
    }

}
