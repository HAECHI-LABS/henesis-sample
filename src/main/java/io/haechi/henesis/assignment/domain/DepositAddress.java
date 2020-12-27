package io.haechi.henesis.assignment.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
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
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "blockchain")
    @Enumerated(EnumType.STRING)
    private Blockchain blockchain;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    // TODO: LocalDateTime
    @Column(name = "created_at")
    private String createdAt;
    // TODO: LocalDateTime
    @Column(name = "updated_at")
    private String updatedAt;

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

    public static DepositAddress of() {
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

    public Transfer transfer(
            String to,
            BigInteger requestedAmount,
            String symbol,
            HenesisClient henesisClient,
            BalanceManager balanceManager
    ) {
        Coin coin = henesisClient.getCoin(symbol);
        Amount amount = Amount.of(requestedAmount, coin.getDecimals());

        if (!balanceManager.hasSpendableBalance(this, amount, symbol)) {
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