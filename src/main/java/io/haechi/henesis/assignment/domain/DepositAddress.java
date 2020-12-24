package io.haechi.henesis.assignment.domain;

import lombok.*;

import javax.persistence.*;
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

    public void withdrawal(Amount amount) {
        this.amount.subtract(amount);
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