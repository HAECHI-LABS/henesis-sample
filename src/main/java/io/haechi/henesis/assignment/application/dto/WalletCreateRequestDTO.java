package io.haechi.henesis.assignment.application.dto;

import lombok.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletCreateRequestDTO{

    @NotNull(message = "Not null MasterWalletId")
    @NotEmpty(message = "Not empty MasterWalletId")
    private String masterWalletId;

}