package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.exception.BadRequestException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Arrays;

@Entity
@Table(name = "deposit_addresses")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositAddress extends DomainEntity {
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
    @Column(name = "master_wallet_address")
    private String masterWalletAddress;

    private DepositAddress(
            String henesisId,
            Status status,
            Blockchain blockchain,
            String name,
            String address,
            String masterWalletAddress
    ) {
        this.henesisId = henesisId;
        this.status = status;
        this.blockchain = blockchain;
        this.name = name;
        this.address = address;
        this.masterWalletAddress = masterWalletAddress;
    }

    public static DepositAddress fromHenesis(
            String henesisId,
            Status status,
            Blockchain blockchain,
            String name,
            String address,
            String masterWalletAddress
    ) {
        return new DepositAddress(
                henesisId,
                status,
                blockchain,
                name,
                address,
                masterWalletAddress
        );
    }

    public Transfer transfer(
            String to,
            BigInteger requestedAmount,
            String symbol,
            HenesisClient henesisClient,
            BalanceManager balanceManager
    ) {
        if (!henesisClient.isSupportedCoin(this.blockchain, symbol)) {
            throw new BadRequestException(String.format("henesis doesn't support '%s' '%s'", blockchain.toString(), symbol));
        }
        Amount amount = Amount.of(requestedAmount);
        balanceManager.validateSpendableBalance(this, amount, symbol);

        Transfer transfer = henesisClient.transfer(to, symbol, amount);
        // 출금 시 실제 트랜잭션이 발생하는 곳은 master wallet
        if (!this.blockchain.equals(Blockchain.BITCOIN)) {
            transfer.setFrom(this.getMasterWalletAddress());
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