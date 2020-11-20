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
    private String walletAddress;
    private String blockchain;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String walletBalance;
    private String masterWalletId;

    @Builder
    public UserWallet(String walletId,
                      String walletName,
                      String walletAddress,
                      String masterWalletId,
                      String blockchain,
                      String status){
        this.walletId = walletId;
        this.walletName = walletName;
        this.walletAddress = walletAddress;
        this.masterWalletId = masterWalletId;
        this.blockchain= blockchain;
        this.status = status;
    }
}
