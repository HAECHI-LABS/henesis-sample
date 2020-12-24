package io.haechi.henesis.assignment.application.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateDepositAddressRequest {

    @NotNull(message = "Wallet Name must be not null")
    @NotEmpty(message = "Wallet Name must be not empty")
    private String name;
}
