package io.haechi.henesis.assignment.domain.transaction;

import io.haechi.henesis.assignment.domain.Amount;
import lombok.*;

import javax.persistence.*;

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
    @Column(name="from_address")
    private String from;
    @Column(name="to_address")
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
            String walletId,
            String walletName
    ){
        this.detailId=detailId;
        this.from=from;
        this.to=to;
        this.amount= amount;
        this.blockchain=blockchain;
        this.status= status;
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
            String walletId,
            String walletName
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
                walletId,
                walletName
                );
    }
    public static Transaction of(
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
            String walletId,
            String walletName
    ) {
        return new Transaction(
                detailId,
                from,
                to,
                amount,
                blockchain,
                status,
                transactionId,
                transactionHash,
                coinSymbol,
                confirmation,
                transferType,
                createdAt,
                updatedAt,
                walletId,
                walletName
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

    public boolean canRollback(){
        return this.isWithdrawal()&&(this.isReverted()||this.isFailed());
    }

    public boolean canDeposit(){
        return this.isDeposit()||this.isConfirmed();
    }

    public boolean isDeposit(){
        return this.transferType.equals("DEPOSIT");
    }

    public boolean isWithdrawal(){
        return this.transferType.equals("WITHDRAWAL");
    }

    public boolean isConfirmed(){
        return this.status.equals("CONFIRMED");
    }
    public boolean isReverted(){
        return this.status.equals("REVERTED");
    }
    public boolean isFailed(){
        return this.status.equals("FAILED");
    }

    public Situation situation(){
        if (this.isDeposit()&&this.isConfirmed())
            return Situation.DEPOSIT_CONFIRMED;

        if (this.isWithdrawal()&&this.isReverted()||this.isFailed())
            return Situation.ROLLBACK;

        return Situation.UPDATE_STATUS;
    }

}
