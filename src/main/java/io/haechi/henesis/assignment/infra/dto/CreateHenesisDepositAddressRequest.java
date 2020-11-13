package io.haechi.henesis.assignment.infra.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateHenesisDepositAddressRequest {
    private String name;
    private String passphrase;

    public CreateHenesisDepositAddressRequest(String name) {
        this.name = name;
    }
}
