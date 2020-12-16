package io.haechi.henesis.assignment.application.btc.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateDepositAddressResponse {
    private String name;

    private CreateDepositAddressResponse(String name) {
        this.name = name;
    }

    public static CreateDepositAddressResponse of(String name) {
        return new CreateDepositAddressResponse(name);
    }
}
