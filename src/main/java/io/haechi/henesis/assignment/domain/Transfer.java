package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.ethklay.Amount;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
    private String to;
    private String walletId;
    private String henesisId;
    private Amount amount;
    private Amount fee;
    // TODO: enum
    private String blockchain;
    // TODO: enum
    private String status;
    // TODO: enum
    private String transferType;
    private String coinSymbol;
    private String hash;
    // TODO: LocalDateTime
    private String createdAt;
    // TODO: LocalDateTime
    private String updatedAt;
    private boolean isFlushed;

    public static Transfer of(
    ) {
        return new Transfer();
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
        return this.transferType.equals("DEPOSIT");
    }

    public boolean isWithdrawal() {
        return this.transferType.equals("WITHDRAWAL");
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
