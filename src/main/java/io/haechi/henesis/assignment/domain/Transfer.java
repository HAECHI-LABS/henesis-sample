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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer extends DomainEntity {
    @Column(name = "from_address")
    private String from;

    @Column(name = "to_address")
    private String to; // btc일 경우 to에 넣고 deposit, withdrawal로 구분

    @Column(name = "deposit_address_id")
    private Long depositAddressId;

    @Column(name = "henesis_transfer_id")
    private String henesisTransferId;

    @Column(name = "henesis_transaction_id")
    private String henesisTransactionId;

    @AttributeOverride(name = "value", column = @Column(name = "amount", precision = 78))
    private Amount amount = new Amount();

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

    @Column(name = "henesis_updated_at")
    private LocalDateTime henesisUpdatedAt;

    @Column(name = "owner")
    @Enumerated(EnumType.STRING)
    private Owner owner;

    private Transfer(
            String henesisTransferId,
            String henesisTransactionId,
            String from,
            String to,
            Amount amount,
            Blockchain blockchain,
            Status status,
            String symbol,
            Type type,
            String hash,
            Owner owner,
            LocalDateTime henesisUpdatedAt
    ) {
        this.henesisTransferId = henesisTransferId;
        this.henesisTransactionId = henesisTransactionId;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.blockchain = blockchain;
        this.status = status;
        this.symbol = symbol;
        this.type = type;
        this.hash = hash;
        this.owner = owner;
        this.henesisUpdatedAt = henesisUpdatedAt;
    }

    private Transfer(
            String henesisTransferId,
            String henesisTransactionId,
            Status status,
            String to,
            Amount amount,
            String symbol,
            Blockchain blockchain,
            String hash,
            Owner owner,
            Type type,
            LocalDateTime henesisUpdatedAt
    ) {
        this.henesisTransferId = henesisTransferId;
        this.henesisTransactionId = henesisTransactionId;
        this.status = status;
        this.to = to;
        this.amount = amount;
        this.symbol = symbol;
        this.blockchain = blockchain;
        this.hash = hash;
        this.owner = owner;
        this.type = type;
        this.henesisUpdatedAt = henesisUpdatedAt;
    }

    private Transfer(
            String henesisTransactionId,
            String symbol,
            Blockchain blockchain,
            Owner owner,
            Type type,
            LocalDateTime henesisUpdatedAt
    ) {
        this.henesisTransactionId = henesisTransactionId;
        this.symbol = symbol;
        this.blockchain = blockchain;
        this.henesisUpdatedAt = henesisUpdatedAt;
        this.owner = owner;
        this.type = type;
    }

    public static Transfer fromHenesis(
            String henesisTransferId,
            String henesisTransactionId,
            String from,
            String to,
            Amount amount,
            Blockchain blockchain,
            Status status,
            String symbol,
            Type type,
            String hash,
            Owner owner,
            LocalDateTime henesisUpdatedAt
    ) {
        return new Transfer(
                henesisTransferId,
                henesisTransactionId,
                from,
                to,
                amount,
                blockchain,
                status,
                symbol,
                type,
                hash,
                owner,
                henesisUpdatedAt
        );
    }

    public static Transfer flush(
            String henesisTransactionId,
            String symbol,
            Blockchain blockchain,
            LocalDateTime henesisUpdatedAt
    ) {
        return new Transfer(
                henesisTransactionId,
                symbol,
                blockchain,
                Owner.MASTER_WALLET,
                Type.FLUSH,
                henesisUpdatedAt
        );
    }

    public static Transfer transfer(
            String henesisTransactionId,
            Status status,
            String to,
            Amount amount,
            String symbol,
            Blockchain blockchain,
            String hash,
            LocalDateTime henesisUpdatedAt
    ) {
        return new Transfer(
                null,
                henesisTransactionId,
                status,
                to,
                amount,
                symbol,
                blockchain,
                hash,
                Owner.MASTER_WALLET,
                Type.WITHDRAWAL,
                henesisUpdatedAt
        );
    }

    public void updateHenesisContext(Transfer transfer) {
        this.status = transfer.getStatus();
        this.henesisUpdatedAt = transfer.getHenesisUpdatedAt();
        this.henesisTransferId = transfer.getHenesisTransferId();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public boolean isFlushed() {
        return this.type.equals(Type.FLUSH);
    }

    public boolean isDeposit() {
        return this.type.equals(Type.DEPOSIT);
    }

    public boolean isWithdrawal() {
        return this.type.equals(Type.WITHDRAWAL);
    }

    public boolean isMasterWallet() {
        return this.owner.equals(Owner.MASTER_WALLET);
    }

    public boolean isDepositAddress() {
        return this.owner.equals(Owner.DEPOSIT_ADDRESS);
    }

    public boolean isWithdrawnFromDepositAddress() {
        return this.depositAddressId != null && this.isWithdrawal();
    }

    public boolean isConfirmed() {
        return this.status.equals(Status.CONFIRMED);
    }

    public enum Status {
        REQUESTED("requested"),
        PENDING("pending"),
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

        public static List<Status> unconfirmed() {
            return Arrays.asList(
                    REQUESTED,
                    MINED
            );
        }
    }

    public enum Type {
        WITHDRAWAL("withdrawal"),
        DEPOSIT("deposit"),
        FLUSH("flush");

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

    public enum Owner {
        MASTER_WALLET("masterWallet"),
        DEPOSIT_ADDRESS("depositAddress");

        private final String name;

        Owner(String name) {
            this.name = name;
        }

        public static Owner of(String name) {
            return Arrays.stream(values())
                    .filter(v -> name.equals(v.name) || name.equalsIgnoreCase(v.name))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException(String.format("'%s' is not supported transfer owner", name)));
        }
    }
}
