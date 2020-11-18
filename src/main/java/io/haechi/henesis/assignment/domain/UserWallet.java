package io.haechi.henesis.assignment.domain;

import lombok.*;

@Getter
@Setter
@Builder
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
}
