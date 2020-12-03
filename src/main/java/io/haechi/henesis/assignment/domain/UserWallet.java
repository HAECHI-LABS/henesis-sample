package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.transaction.Action;
import io.haechi.henesis.assignment.domain.transaction.ActionSupplier;
import io.haechi.henesis.assignment.domain.transaction.Situation;
import io.haechi.henesis.assignment.domain.transaction.Transaction;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWallet {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String walletId;
    private String name;
    private String address;
    private String blockchain;
    private Amount balance;
    private String masterWalletId;
    private String status;
    private String createdAt;
    private String updatedAt;


    private UserWallet(
            String walletId,
            String name,
            String address,
            String blockchain,
            String status,
            Amount balance,
            String masterWalletId,
            String createdAt,
            String updatedAt
            ) {
        this.walletId = walletId;
        this.name = name;
        this.address = address;
        this.blockchain = blockchain;
        this.status = status;
        this.balance = balance;
        this.masterWalletId =masterWalletId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserWallet of(
            String walletId,
            String name,
            String address,
            String blockchain,
            String status,
            Amount balance,
            String masterWalletId,
            String createdAt,
            String updatedAt
            ){
        return new UserWallet(
                walletId,
                name,
                address,
                blockchain,
                status,
                balance,
                masterWalletId,
                createdAt,
                updatedAt
                );
    }

    private UserWallet(
            String walletId,
            String name,
            String address,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ) {
        this.walletId = walletId;
        this.name = name;
        this.address = address;
        this.blockchain = blockchain;
        this.status = status;
        this.createdAt =createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserWallet of(
            String walletId,
            String name,
            String address,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ){
        return new UserWallet(
                walletId,
                name,
                address,
                blockchain,
                status,
                createdAt,
                updatedAt
        );
    }


    public void increaseBalanceBy(Amount amount) {
        this.balance.add(amount);
    }


}
