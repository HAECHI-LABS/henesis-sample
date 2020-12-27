package io.haechi.henesis.assignment.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "balances")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Balance {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "deposit_address_id", foreignKey = @ForeignKey(name = "fk_balances_deposit_address_id"))
    private DepositAddress depositAddress;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "amount")
    private Amount amount;
    // TODO: LocalDateTime
    @Column(name = "created_at")
    private String createdAt;
    // TODO: LocalDateTime
    @Column(name = "updated_at")
    private String updatedAt;

    private Balance(DepositAddress depositAddress, String symbol, Amount amount) {
        this.depositAddress = depositAddress;
        this.symbol = symbol;
        this.amount = amount;
    }

    public static Balance zero(DepositAddress depositAddress, String symbol) {
        return new Balance(depositAddress, symbol, Amount.zero());
    }

    public void add(Amount amount) {
        this.amount.add(amount);
    }

    public void subtract(Amount amount) {
        this.amount.subtract(amount);
    }

    public boolean hasSpendableAmount(Amount requestedAmount, Amount unconfirmedAmount) {
        return this.amount.isSpendableAmount(requestedAmount.add(unconfirmedAmount));
    }
}
