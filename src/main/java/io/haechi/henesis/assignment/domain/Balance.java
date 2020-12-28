package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.exception.InternalServerException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "balances")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Balance extends DomainEntity {
    @ManyToOne
    @JoinColumn(name = "deposit_address_id", foreignKey = @ForeignKey(name = "fk_balances_deposit_address_id"))
    private DepositAddress depositAddress;

    @Column(name = "symbol")
    private String symbol;

    @AttributeOverride(name = "value", column = @Column(name = "amount", precision = 78))
    private Amount amount = new Amount();

    private Balance(DepositAddress depositAddress, String symbol, Amount amount) {
        super();
        this.depositAddress = depositAddress;
        this.symbol = symbol;
        this.amount = amount;
    }

    public static Balance of(DepositAddress depositAddress, String symbol, Amount amount) {
        return new Balance(depositAddress, symbol, amount);
    }

    public static Balance zero(DepositAddress depositAddress, String symbol) {
        return new Balance(depositAddress, symbol, Amount.zero());
    }

    public void changeAmount(Amount amount) {
        this.amount = amount;
    }

    public void add(Amount amount) {
        this.amount = this.amount.add(amount);
    }

    public void subtract(Amount amount) {
        if (this.amount.compareTo(amount) < 0) {
            throw new InternalServerException("balance cannot be negative");
        }
        this.amount = this.amount.subtract(amount);
    }
}
