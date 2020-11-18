package io.haechi.henesis.assignment.application.dto;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWalletDTO {

    private String walletId;
    private String txId;
    private String balance;

}
