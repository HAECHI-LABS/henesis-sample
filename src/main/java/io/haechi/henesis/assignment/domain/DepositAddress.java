package io.haechi.henesis.assignment.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;

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
    private Long id;
    @Column(name = "henesis_id")
    private String henesisId;
    // TODO: enum
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    // TODO: enum
    @Column(name = "blockchain")
    @Enumerated(EnumType.STRING)
    private Blockchain blockchain;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "amount")
    private Amount amount = new Amount();
    // TODO: LocalDateTime
    @Column(name = "created_at")
    private String createdAt;
    // TODO: LocalDateTime
    @Column(name = "updated_at")
    private String updatedAt;

    public void deposit(Amount amount) {
        this.amount.add(amount);
    }

    public void withdraw(Amount amount) {
        this.amount.subtract(amount);
    }

    // TODO 별로임
    public boolean hasSpendableAmount(
            Amount requestAmount,
            Amount unconfirmedAmount,
            Amount estimatedFee
    ) {
        return this.amount.isSpendableAmount(requestAmount.add(unconfirmedAmount).add(estimatedFee));
    }

    public Transfer transfer(
            String to,
            BigInteger requestedAmount,
            String symbol,
            HenesisClient henesisClient,
            BalanceValidator balanceValidator
    ) {
        Coin coin = henesisClient.getCoin(symbol);
        Amount amount = Amount.of(requestedAmount, coin.getDecimals());

        if (!balanceValidator.validate(this, amount, symbol)) {
            // TODO: log
            throw new IllegalStateException("there is no spendable balance");
        }

        Transfer transfer = henesisClient.transfer(to, symbol, amount);
        if (!this.blockchain.equals(Blockchain.BITCOIN)) {
            transfer.setFrom(this.getAddress());
        }
        transfer.setDepositAddressId(this.getId());
        return transfer;
    }

    public static DepositAddress of(){
        return new DepositAddress();
    }
    public static DepositAddress fromHenesis(
            String henesisId,
            Status status,
            Blockchain blockchain,
            String name,
            String address
    ) {
        return new DepositAddress(
                henesisId,
                status,
                blockchain,
                name,
                address
        );
    }

    private DepositAddress(
            String henesisId,
            Status status,
            Blockchain blockchain,
            String name,
            String address
    ) {
        this.henesisId = henesisId;
        this.status = status;
        this.blockchain = blockchain;
        this.name = name;
        this.address = address;
    }

    public void increaseBalanceBy(Amount amount) {
    }

    // btc
    public void withdrawBy(Amount amount, Amount estimatedFee, Amount walletBalance) {
    }

    public void withdrawBy(Amount amount, Amount walletBalance) {
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        ACTIVE("active"),
        CREATING("creating"),
        FAILED("failed");

        private final String name;

        Status(String name) {
            this.name = name;
        }

        public static Status of(String name) {
            return Arrays.stream(values())
                    .filter(v -> name.equals(v.name) || name.equalsIgnoreCase(v.name))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException(String.format("'%s' is not supported deposit address status", name)));
        }
    }
}