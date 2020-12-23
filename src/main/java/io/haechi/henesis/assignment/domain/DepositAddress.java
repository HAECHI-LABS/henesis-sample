package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.ethklay.Amount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposit_addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositAddress {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "deposit_address_id")
    private String depositAddressId;
    @Column(name = "master_wallet_id")
    private String masterWalletId;
    // TODO: enum
    @Column(name = "status")
    private String status;
    // TODO: enum
    @Column(name = "blockchain")
    private String blockchain;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "amount")
    private Amount amount;
    @Column(name = "pub")
    private String pub;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static DepositAddress of(
    ) {
        return new DepositAddress();
    }


    public void increaseBalanceBy(Amount amount) {
    }

    // btc
    public void withdrawBy(Amount amount, Amount estimatedFee, Amount walletBalance) {
    }

    public void withdrawBy(Amount amount, Amount walletBalance) {
    }
}