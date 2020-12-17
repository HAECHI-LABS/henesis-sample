package io.haechi.henesis.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.henesis-wallet")
public class HenesisWalletProperties {
    private String url;
    private String apiSecret;
    private String accessToken;

    private String ethPassphrase;
    private String klayPassphrase;
    private String btcPassphrase;

    private String ethMasterWalletId;
    private String klayMasterWalletId;
    private String btcWalletId;

    private String ethSize;
    private String klaySize;
    private String btcSize;

}
