package io.haechi.henesis.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.henesis-wallet")
public class HenesisWalletProperties {
    private String url;
    private String apiSecret;
    private String accessToken;
    private String passphrase;
    private String masterWalletId;
}
