package io.haechi.henesis.assignment.domain;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class BalanceManager {
    private final TransferRepository transferRepository;
    private final BalanceRepository balanceRepository;

    public BalanceManager(
            TransferRepository transferRepository,
            BalanceRepository balanceRepository
    ) {
        this.transferRepository = transferRepository;
        this.balanceRepository = balanceRepository;
    }

    public void validateSpendableBalance(
            DepositAddress depositAddress,
            Amount requestedAmount,
            String symbol
    ) {
        Amount unconfirmedAmount = Amount.of(
                this.transferRepository.sumUnconfirmedAmount(
                        depositAddress.getId(),
                        depositAddress.getBlockchain(),
                        symbol,
                        Transfer.Status.unconfirmed()
                ).orElse(BigInteger.ZERO)
        );
        Balance balance = balanceRepository.findByDepositAddressAndSymbol(depositAddress, symbol)
                .orElse(Balance.zero(depositAddress, symbol));

        if (balance.getAmount().compareTo(unconfirmedAmount) < 0 || balance.getAmount().subtract(unconfirmedAmount).compareTo(requestedAmount) < 0) {
            throw new IllegalStateException(
                    String.format(
                            "there is no spendable balance at '%s'. '%s' has '%s' spendable balance and requested amount is '%s'",
                            symbol,
                            depositAddress.getAddress(),
                            balance.getAmount().compareTo(unconfirmedAmount) < 0
                                    ? BigInteger.ZERO
                                    : balance.getAmount().subtract(unconfirmedAmount),
                            requestedAmount
                    )
            );
        }
    }

    // TODO: naming
    public void reflectTransfer(Transfer transfer, DepositAddress depositAddress) {
        Balance balance = this.balanceRepository.findByDepositAddressAndSymbol(depositAddress, transfer.getSymbol())
                .orElse(Balance.zero(depositAddress, transfer.getSymbol()));

        if (transfer.isDeposit()) {
            balance.add(transfer.getAmount());
            this.balanceRepository.save(balance);
            return;
        }
        balance.subtract(transfer.getAmount());
        this.balanceRepository.save(balance);
    }
}
