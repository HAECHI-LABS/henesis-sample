package io.haechi.henesis.assignment.application.dto;

import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.Transfer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferDto {
    private Long id;
    private Transfer.Status status;
    private String to;
    private Amount amount;
    private Blockchain blockchain;
    private Long depositAddressId;

    public TransferDto(Transfer transfer) {
        this.id = transfer.getId();
        this.status = transfer.getStatus();
        this.to = transfer.getTo();
        this.amount = transfer.getAmount();
        this.blockchain = transfer.getBlockchain();
        this.depositAddressId = transfer.getDepositAddressId();
    }
}
