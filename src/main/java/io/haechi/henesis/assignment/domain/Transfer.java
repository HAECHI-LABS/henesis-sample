package io.haechi.henesis.assignment.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_address")
    private String from;

    @Column(name = "to_address")
    private String to; // btc일 경우 to에 넣고 deposit, withdrawal로 구분

    @Column(name = "deposit_address_id")
    private Long depositAddressId;

    @Column(name = "henesis_id")
    private String henesisId;

    @AttributeOverride(name = "value", column = @Column(name = "amount"))
    private Amount amount = new Amount();

    @AttributeOverride(name = "value", column = @Column(name = "fee"))
    private Amount fee;

    @Column(name = "blockchain")
    @Enumerated(EnumType.STRING)
    private Blockchain blockchain;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "hash")
    private String hash;

    // TODO: LocalDateTime
    @Column(name = "created_at")
    private String createdAt;
    // TODO: LocalDateTime

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "henesis_updated_at")
    private String henesisUpdatedAt;

    @Column(name = "is_flushed")
    private boolean isFlushed;

    public static Transfer ready(
            String from,
            String to,
            Amount amount,
            String symbol,
            Blockchain blockchain,
            Long depositAddressId
    ) {
        return new Transfer(
                from,
                to,
                amount,
                symbol,
                blockchain,
                depositAddressId
        );
    }

    private Transfer(
            String from,
            String to,
            Amount amount,
            String symbol,
            Blockchain blockchain,
            Long depositAddressId
    ) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.symbol = symbol;
        this.blockchain = blockchain;
        this.depositAddressId = depositAddressId;
        this.status = Status.READY;
        this.type = Type.WITHDRAWAL;
        this.isFlushed = false;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void syncWithHenesis(Transfer transfer) {
        this.henesisId = transfer.getHenesisId();
        this.status = transfer.getStatus();
        this.hash = transfer.getHash();
        this.henesisUpdatedAt = transfer.getHenesisUpdatedAt();
    }

    public static Transfer of() {
        return new Transfer();
    }

    public static Transfer fromHenesis(
            String henesisId,
            String from,
            String to,
            Amount amount,
            Blockchain blockchain,
            Status status,
            String symbol,
            Type type,
            String hash,
            String henesisUpdatedAt
    ) {
        return new Transfer(
                henesisId,
                from,
                to,
                amount,
                blockchain,
                status,
                symbol,
                type,
                hash,
                henesisUpdatedAt
        );
    }

    private Transfer(
            String henesisId,
            String from,
            String to,
            Amount amount,
            Blockchain blockchain,
            Status status,
            String symbol,
            Type type,
            String hash,
            String henesisUpdatedAt
    ) {
        this.henesisId = henesisId;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.blockchain = blockchain;
        this.status = status;
        this.symbol = symbol;
        this.type = type;
        this.hash = hash;
        this.henesisUpdatedAt = henesisUpdatedAt;
    }

    public static Transfer transfer(
            String henesisId,
            Status status,
            Blockchain blockchain,
            String hash
    ) {
        return new Transfer(
                henesisId,
                status,
                blockchain,
                hash
        );
    }

    private Transfer(
            String henesisId,
            Status status,
            Blockchain blockchain,
            String hash
    ) {
        this.henesisId = henesisId;
        this.status = status;
        this.blockchain = blockchain;
        this.hash = hash;
    }

    public static Transfer newInstanceOf(
            String transactionId,
            String blockchain,
            String status,
            String createdAt
    ) {
        return new Transfer(
//                transactionId,
//                blockchain,
//                status,
//                createdAt
        );

    }

    // BTC
    public boolean isDesired() {
        return this.isConfirmed() || this.isReverted() || this.isFailed();
    }

    public boolean isDeposit() {
        return this.type.equals(Type.DEPOSIT);
    }

    public boolean isWithdrawal() {
        return this.type.equals(Type.WITHDRAWAL);
    }

    public boolean isConfirmed() {
        return this.status.equals(Status.CONFIRMED);
    }

    public boolean isReverted() {
        return this.status.equals(Status.REVERTED);
    }

    public boolean isFailed() {
        return this.status.equals(Status.FAILED);
    }

    public Situation situation() {
        if (this.isDeposit() && this.isConfirmed()) {
            return Situation.DEPOSIT_BTC;
        }
        if (this.isWithdrawal() && this.isReverted() || this.isFailed()) {
            return Situation.ROLLBACK_BTC;
        }

        return Situation.NOTHING_TO_DO;
    }

    public enum Status {
        READY("ready"),
        REQUESTED("requested"),
        FAILED("failed"),
        MINED("mined"),
        REVERTED("reverted"),
        CONFIRMED("confirmed");

        private final String name;

        Status(String name) {
            this.name = name;
        }

        public static Status of(String name) {
            return Arrays.stream(values())
                    .filter(v -> name.equals(v.name) || name.equalsIgnoreCase(v.name))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException(String.format("'%s' is not supported transfer status", name)));
        }
    }

    public enum Type {
        WITHDRAWAL("withdrawal"),
        DEPOSIT("deposit");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public static Type of(String name) {
            return Arrays.stream(values())
                    .filter(v -> name.equals(v.name) || name.equalsIgnoreCase(v.name))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException(String.format("'%s' is not supported transfer type", name)));
        }
    }
}
