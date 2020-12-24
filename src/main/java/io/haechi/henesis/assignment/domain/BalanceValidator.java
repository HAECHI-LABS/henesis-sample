package io.haechi.henesis.assignment.domain;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class BalanceValidator {
    private final TransferRepository transferRepository;
    private final HenesisClientSupplier henesisClientSupplier;

    public BalanceValidator(
            TransferRepository transferRepository,
            HenesisClientSupplier henesisClientSupplier
    ) {
        this.transferRepository = transferRepository;
        this.henesisClientSupplier = henesisClientSupplier;
    }

    public boolean validate(
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

        Amount estimatedFee = this.henesisClientSupplier.supply(depositAddress.getBlockchain()).getEstimatedFee();

        return depositAddress.hasSpendableAmount(
                requestedAmount,
                unconfirmedAmount,
                estimatedFee
        );
    }
}
