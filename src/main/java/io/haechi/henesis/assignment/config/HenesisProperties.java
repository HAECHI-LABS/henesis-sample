package io.haechi.henesis.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.henesis")
public class HenesisProperties {
    private String url;
    private String apiSecret;
    private String accessToken;

    private String ethPassphrase;
    private String klayPassphrase;
    private String btcPassphrase;

    private String ethMasterWalletId;
    private String klayMasterWalletId;
    private String btcMasterWalletId;

    private Integer ethPollingSize = 100;
    private Integer klayPollingSize = 100;
    private Integer btcPollingSize = 100;

    private Long ethPollingInterval = 10000L;
    private Long klayPollingInterval = 3000L;
    private Long btcPollingInterval = 180000L;
}
