package io.haechi.henesis.assignment.domain.btc;

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
    private BtcAmount balance;
    private String pub;
    private String createdAt;

    private DepositAddress(
            String depositAddressId,
            String name,
            String address,
            BtcAmount balance,
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
                BtcAmount.of(BigInteger.ZERO),
                pub,
                createdAt
        );
    }


    public void increaseBalanceBy(BtcAmount amount) {
        this.balance.add(amount);
    }

    public void withdrawBy(BtcAmount amount, BtcAmount estimatedFee, BtcAmount walletBalance) {
        this.getBalance().subtractBy(amount, estimatedFee, walletBalance);
    }
}