package io.haechi.henesis.assignment.application.dto;

import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.DepositAddress;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositAddressDto {
    private Long id;
    private String henesisId;
    private DepositAddress.Status status;
    private Blockchain blockchain;
    private String name;
    private String address;

    public DepositAddressDto(DepositAddress depositAddress) {
        this.id = depositAddress.getId();
        this.henesisId = depositAddress.getHenesisId();
        this.status = depositAddress.getStatus();
        this.blockchain = depositAddress.getBlockchain();
        this.name = depositAddress.getName();
        this.address = depositAddress.getAddress();
    }
}
