package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.HenesisClientSupplier;
import io.haechi.henesis.assignment.infra.BtcHenesisClient;
import io.haechi.henesis.assignment.infra.EthKlayHenesisClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableConfigurationProperties({HenesisProperties.class})
public class HenesisConfig {
    @Bean
    public HenesisProperties henesisProperties() {
        return new HenesisProperties();
    }

    @Bean
    @Qualifier("ethHenesisClient")
    public HenesisClient ethHenesisClient(
            HenesisProperties henesisProperties,
            @Qualifier("henesisRestTemplate") RestTemplate restTemplate
    ) {
        return new EthKlayHenesisClient(
                restTemplate,
                henesisProperties.getEthMasterWalletId(),
                henesisProperties.getEthPassphrase(),
                Blockchain.ETHEREUM
        );
    }

    @Bean
    @Qualifier("klayHenesisClient")
    public HenesisClient klayHenesisClient(
            HenesisProperties henesisProperties,
            @Qualifier("henesisRestTemplate") RestTemplate restTemplate
    ) {
        return new EthKlayHenesisClient(
                restTemplate,
                henesisProperties.getKlayMasterWalletId(),
                henesisProperties.getKlayPassphrase(),
                Blockchain.KLAYTN
        );
    }

    @Bean
    @Qualifier("btcHenesisClient")
    public HenesisClient btcHenesisClient(
            HenesisProperties henesisProperties,
            @Qualifier("henesisRestTemplate") RestTemplate restTemplate
    ) {
        return new BtcHenesisClient(
                restTemplate,
                henesisProperties.getBtcMasterWalletId(),
                henesisProperties.getBtcPassphrase()
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
}
