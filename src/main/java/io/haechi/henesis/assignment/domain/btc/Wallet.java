package io.haechi.henesis.assignment.domain.btc;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {
    private String walletId;
    private String name;
    private String address;
    private String orgId;
    private String keyAddress;
    private String keyFile;
    private String pub;
    private String encryptionKey;
    private String status;
    private boolean whitelistActivated;
    private String createdAt;


    private Wallet(
            String walletId,
            String name,
            String address,
            String orgId,
            String keyAddress,
            String keyFile,
            String pub,
            String encryptionKey,
            String status,
            boolean whitelistActivated,
            String createdAt
    ) {
        this.walletId = walletId;
        this.name = name;
        this.address = address;
        this.orgId = orgId;
        this.keyAddress = keyAddress;
        this.keyFile = keyFile;
        this.pub = pub;
        this.encryptionKey = encryptionKey;
        this.status = status;
        this.whitelistActivated = whitelistActivated;
        this.createdAt = createdAt;
    }

    public static Wallet of(
            String walletId,
            String name,
            String address,
            String orgId,
            String keyAddress,
            String keyFile,
            String pub,
            String encryptionKey,
            String status,
            boolean whitelistActivated,
            String createdAt
    ) {
        return new Wallet(
                walletId,
                name,
                address,
                orgId,
                keyAddress,
                keyFile,
                pub,
                encryptionKey,
                status,
                whitelistActivated,
                createdAt
        );
    }

}
