package io.haechi.henesis.assignment.domain.btc;

import io.haechi.henesis.assignment.domain.Situation;
import io.haechi.henesis.assignment.domain.ethklay.Amount;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransaction {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String walletId;
    private String feeAmount;
    private String receivedAt;
    private String sendTo;
    private String type;
    private String status;
    private Amount amount;

    private String transactionId;
    private String transactionHash;
    private String createdAt;
    private String updatedAt;

    private BtcTransaction(
            String walletId,
            String feeAmount,
            String receivedAt,
            String sendTo,
            String type,
            String status,
            Amount amount,
            String transactionId,
            String transactionHash,
            String createdAt,
            String updatedAt
    ) {
        this.walletId = walletId;
        this.feeAmount = feeAmount;
        this.receivedAt = receivedAt;
        this.sendTo = sendTo;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.transactionId = transactionId;
        this.transactionHash = transactionHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BtcTransaction of(
            String walletId,
            String feeAmount,
            String receivedAt,
            String sendTo,
            String type,
            String status,
            Amount amount,
            String transactionId,
            String transactionHash,
            String createdAt,
            String updatedAt
    ) {
        return new BtcTransaction(
                walletId,
                feeAmount,
                receivedAt,
                sendTo,
                type,
                status,
                amount,
                transactionId,
                transactionHash,
                createdAt,
                updatedAt
        );
    }

    public boolean isDesired() {
        return this.isConfirmed() || this.isReverted() || this.isFailed();
    }

    public boolean isDeposit() {
        return this.type.equals("DEPOSIT");
    }

    public boolean isWithdrawal() {
        return this.type.equals("WITHDRAWAL");
    }

    public boolean isConfirmed() {
        return this.status.equals("CONFIRMED");
    }

    public boolean isReverted() {
        return this.status.equals("REVERTED");
    }

    public boolean isFailed() {
        return this.status.equals("FAILED");
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
}
