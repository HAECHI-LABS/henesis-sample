package io.haechi.henesis.assignment.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWallet {
    private int id;
    private String walletId;
    private String walletName;
    private String walletBalance;
    private String walletAddress;
    private String masterWalletId;
    private String createdAt;
    private String updatedAt;
    private String blockchain;

    @Builder
    public UserWallet(String walletId,
                      String walletName,
                      String walletAddress,
                      String masterWalletId,
                      String blockchain){
        this.walletId = walletId;
        this.walletName = walletName;
        this.walletAddress = walletAddress;
        this.masterWalletId = masterWalletId;
        this.blockchain= blockchain;
    }

}
