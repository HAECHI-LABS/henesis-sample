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
    private String address;

    private CreateDepositAddressResponse(
            String name,
            String address
    ) {
        this.name = name;
        this.address = address;
    }

    public static CreateDepositAddressResponse of(String name, String address) {
        return new CreateDepositAddressResponse(
                name,
                address
        );
    }
}
