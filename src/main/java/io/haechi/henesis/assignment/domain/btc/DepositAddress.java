package io.haechi.henesis.assignment.domain.btc;

import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.ethklay.Wallet;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositAddress {

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String depositAddressId;
    private String name;
    private String address;
    private Amount balance;
    private String pub;
    private String createdAt;

    private DepositAddress(
            String depositAddressId,
            String name,
            String address,
            Amount balance,
            String pub,
            String createdAt
    ) {
        this.depositAddressId = depositAddressId;
        this.name = name;
        this.address = address;
        this.balance = balance;
        this.pub = pub;
        this.createdAt = createdAt;
    }

    public static DepositAddress of(
            String depositAddressId,
            String name,
            String address,
            String pub,
            String createdAt
    ) {
        return new DepositAddress(
                depositAddressId,
                name,
                address,
                Amount.of(BigInteger.ZERO),
                pub,
                createdAt
        );
    }


    public void increaseBalanceBy(Amount amount) {
        this.balance.add(amount);
    }

}