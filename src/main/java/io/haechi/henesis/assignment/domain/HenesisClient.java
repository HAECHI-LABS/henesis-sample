package io.haechi.henesis.assignment.domain;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface HenesisClient {
    DepositAddress createDepositAddress(String name);

    Transfer transfer(String to, String symbol, Amount amount);

    Amount getEstimatedFee();

    List<Transfer> getLatestTransfersByUpdatedAtGte(LocalDateTime updatedAtGte, int size);

    Pagination<Transfer> getTransfersByUpdatedAtGte(LocalDateTime updatedAtGte, Pageable pageable);

    List<Balance> getDepositAddressBalances(DepositAddress depositAddress);

    Pagination<DepositAddress> getDepositAddresses(Pageable pageable);

    Transfer flush(List<String> depositAddressHenesisIds);

    DepositAddress getDepositAddress(String id);

    Coin getCoin(String symbol);

    boolean isSupportedCoin(Blockchain blockchain, String symbol);

    String getMasterWalletAddress();
}
