package io.haechi.henesis.assignment.domain;

public interface MonitoringService {


    void getValueTransferEvents(
            String walletId,
            String masterWalletId,
            String transactionId,
            String updatedAtGte,
            String status,
            String size);
}
