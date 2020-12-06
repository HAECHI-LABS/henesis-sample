package io.haechi.henesis.assignment.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {
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


    private Wallet(
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

    public static Wallet of(
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
        return new Wallet(
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

    private Wallet(
            String walletId,
            String name,
            String address,
            Amount balance,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ) {
        this.walletId = walletId;
        this.name = name;
        this.address = address;
        this.balance = balance;
        this.blockchain = blockchain;
        this.status = status;
        this.createdAt =createdAt;
        this.updatedAt = updatedAt;
    }

    public static Wallet of(
            String walletId,
            String name,
            String address,
            Amount balance,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ){
        return new Wallet(
                walletId,
                name,
                address,
                balance,
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
