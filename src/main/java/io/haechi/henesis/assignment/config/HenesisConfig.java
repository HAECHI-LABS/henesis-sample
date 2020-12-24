package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.HenesisClientSupplier;
import io.haechi.henesis.assignment.infra.btc.BtcHenesisClient;
import io.haechi.henesis.assignment.infra.ethklay.EthKlayHenesisClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableConfigurationProperties({HenesisProperties.class})
// TODO: polish
public class HenesisConfig {
    @Bean
    public HenesisProperties henesisProperties() {
        return new HenesisProperties();
    }

    @Bean
    @Qualifier("ethHenesisClient")
    public HenesisClient ethHenesisClient(
            @Qualifier("restTemplate") RestTemplate restTemplate,
            @Qualifier("ethMasterWalletId") String ethMasterWalletId,
            @Qualifier("ethPassphrase") String ethPassphrase,
            @Qualifier("ethSize") String ethSize
    ) {
        return new EthKlayHenesisClient(
                restTemplate,
                ethMasterWalletId,
                ethPassphrase,
                ethSize,
                Blockchain.ETHEREUM
        );
    }

    @Bean
    @Qualifier("klayHenesisClient")
    public HenesisClient klayHenesisClient(
            @Qualifier("henesisRestTemplate") RestTemplate restTemplate,
            @Qualifier("klayMasterWalletId") String klayMasterWalletId,
            @Qualifier("klayPassphrase") String klayPassphrase,
            @Qualifier("klaySize") String klaySize
    ) {
        return new EthKlayHenesisClient(
                restTemplate,
                klayMasterWalletId,
                klayPassphrase,
                klaySize,
                Blockchain.ETHEREUM
        );
    }

    @Bean
    @Qualifier("btcHenesisClient")
    public HenesisClient btcHenesisClient(
            @Qualifier("henesisRestTemplate") RestTemplate restTemplate,
            @Qualifier("btcMasterWalletId") String btcMasterWalletId,
            @Qualifier("btcPassphrase") String btcPassphrase,
            @Qualifier("btcSize") String btcSize
    ) {
        return new BtcHenesisClient(
                restTemplate,
                btcMasterWalletId,
                btcPassphrase,
                btcSize
        );
    }

    @Bean
    public HenesisClientSupplier henesisClientSupplier(
            @Qualifier("ethHenesisClient") HenesisClient ethHenesisClient,
            @Qualifier("klayHenesisClient") HenesisClient klayHenesisClient,
            @Qualifier("btcHenesisClient") HenesisClient btcHenesisClient
    ) {
        Map<Blockchain, HenesisClient> map = new HashMap<>();
        map.put(Blockchain.ETHEREUM, ethHenesisClient);
        map.put(Blockchain.KLAYTN, klayHenesisClient);
        map.put(Blockchain.BITCOIN, btcHenesisClient);
        return new HenesisClientSupplier(map);
    }

    @Bean
    @Qualifier("walletUrl")
    public String url(HenesisProperties henesisProperties) {
        return henesisProperties.getUrl();
    }

    @Bean
    @Qualifier("walletApiSecret")
    public String apiSecret(HenesisProperties henesisProperties) {
        return henesisProperties.getApiSecret();
    }

    @Bean
    @Qualifier("walletAccessToken")
    public String accessToken(HenesisProperties henesisProperties) {
        return henesisProperties.getAccessToken();
    }


    @Bean
    @Qualifier("ethMasterWalletId")
    public String ethMasterWalletId(HenesisProperties henesisProperties) {
        return henesisProperties.getEthMasterWalletId();
    }

    @Bean
    @Qualifier("klayMasterWalletId")
    public String klayMasterWalletId(HenesisProperties henesisProperties) {
        return henesisProperties.getKlayMasterWalletId();
    }

    @Bean
    @Qualifier("btcMasterWalletId")
    public String btcMasterWalletId(HenesisProperties henesisProperties) {
        return henesisProperties.getBtcMasterWalletId();
    }


    @Bean
    @Qualifier("ethPassphrase")
    public String ethPassphrase(HenesisProperties henesisProperties) {
        return henesisProperties.getEthPassphrase();
    }

    @Bean
    @Qualifier("klayPassphrase")
    public String klayPassphrase(HenesisProperties henesisProperties) {
        return henesisProperties.getKlayPassphrase();
    }

    @Bean
    @Qualifier("btcPassphrase")
    public String btcPassphrase(HenesisProperties henesisProperties) {
        return henesisProperties.getBtcPassphrase();
    }

    @Bean
    @Qualifier("ethSize")
    public String ethSize(HenesisProperties henesisProperties) {
        return henesisProperties.getEthSize();
    }

    @Bean
    @Qualifier("klaySize")
    public String klaySize(HenesisProperties henesisProperties) {
        return henesisProperties.getKlaySize();
    }

    @Bean
    @Qualifier("btcSize")
    public String btcSize(HenesisProperties henesisProperties) {
        return henesisProperties.getBtcSize();
    }
}
