package io.haechi.henesis.assignment.application.dto;

import io.haechi.henesis.assignment.domain.DepositAddress;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateDepositAddressResponse {
    private Long id;
    private String name;
    private String address;

    private CreateDepositAddressResponse(Long id, String name, String address) {
        this.name = name;
        this.id = id;
        this.address = address;
    }

    public static CreateDepositAddressResponse of(DepositAddress depositAddress) {
        return new CreateDepositAddressResponse(
                depositAddress.getId(),
                depositAddress.getName(),
                depositAddress.getAddress()
        );
    }
}
