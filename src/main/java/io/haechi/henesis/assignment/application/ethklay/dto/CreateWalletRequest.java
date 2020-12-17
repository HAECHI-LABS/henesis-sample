package io.haechi.henesis.assignment.application.ethklay.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateWalletRequest {

    @NotNull(message = "walletName must be not null")
    @NotEmpty(message = "walletName must be not empty")
    private String name;
}
