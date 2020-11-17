package io.haechi.henesis.assignment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWalletDTO {

    private String walletId;
    private String txId;
    private String balance;

}
