package io.haechi.henesis.assignment.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int detailId;
    private String from;
    private String to;
    private Amount amount;
    private String blockchain;
    private String status;
    private String transferType;
    private String coinSymbol;
    private String confirmation;
    private String transactionId;
    private String transactionHash;
    private String createdAt;
    private String updatedAt;

    private String walletId;
    private String walletName;

    private Transaction(
            int detailId,
            String from,
            String to,
            Amount amount,
            String blockchain,
            String status,
            String transactionId,
            String transactionHash,
            String coinSymbol,
            String confirmation,
            String transferType,
            String createdAt,
            String updatedAt,
            String walletName,
            String walletId
    ){
        this.detailId=detailId;
        this.from=from;
        this.to=to;
        this.amount= amount;
        this.blockchain=blockchain;
        this.status=status;
        this.transactionId=transactionId;
        this.transactionHash=transactionHash;
        this.coinSymbol=coinSymbol;
        this.confirmation=confirmation;
        this.transferType=transferType;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.walletId=walletId;
        this.walletName=walletName;
    }

    public static Transaction of(
            int detailId,
            String from,
            String to,
            String amount,
            String blockchain,
            String status,
            String transactionId,
            String transactionHash,
            String coinSymbol,
            String confirmation,
            String transferType,
            String createdAt,
            String updatedAt,
            String walletName,
            String walletId
    ) {
        return new Transaction(
                detailId,
                from,
                to,
                Amount.of(amount),
                blockchain,
                status,
                transactionId,
                transactionHash,
                coinSymbol,
                confirmation,
                transferType,
                createdAt,
                updatedAt,
                walletName,
                walletId
                );
    }

    private Transaction(
            String transactionId,
            String blockchain,
            String status,
            String createdAt
    ){
        this.transactionId = transactionId;
        this.blockchain =blockchain;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Transaction newInstanceOf(
            String transactionId,
            String blockchain,
            String status,
            String createdAt
    ){
        return new Transaction(
                transactionId,
                blockchain,
                status,
                createdAt
        );

    }

    public boolean isConfirmed(){
        return this.status.contains("CONFIRMED");
    }
    public boolean isReverted(){
        return this.status.contains("REVERTED");
    }
    public boolean isFailed(){
        return this.status.contains("FAILED");
    }
    public boolean isDeposit(){
        return this.transferType.contains("DEPOSIT");
    }
    public boolean isWithdrawal(){
        return this.transferType.contains("WITHDRAWAL");
    }
}
