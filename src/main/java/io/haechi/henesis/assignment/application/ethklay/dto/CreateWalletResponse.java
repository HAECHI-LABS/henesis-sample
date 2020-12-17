package io.haechi.henesis.assignment.application.ethklay.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateWalletResponse {

    private String name;

    private CreateWalletResponse(String name) {
        this.name = name;
    }

    public static CreateWalletResponse of(String name) {
        return new CreateWalletResponse(name);
    }
}
