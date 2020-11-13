package io.haechi.henesis.assignment.application.dto;

import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.Transfer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushDto {
    private Long id;
    private Transfer.Status status;
    private Blockchain blockchain;

    public FlushDto(Transfer transfer) {
        this.id = transfer.getId();
        this.status = transfer.getStatus();
        this.blockchain = transfer.getBlockchain();
    }
}
