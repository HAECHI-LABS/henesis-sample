package io.haechi.henesis.assignment.application.ethklay.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateWalletResponse {

    private String id;
    private String name;
    private String address;
    private String blockchain;
    private String status;
    private String createdAt;
    private String updatedAt;


    private CreateWalletResponse(
            String id,
            String name,
            String address,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.blockchain = blockchain;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CreateWalletResponse of(
            String id,
            String name,
            String address,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ) {
        return new CreateWalletResponse(
                id,
                name,
                address,
                blockchain,
                status,
                createdAt,
                updatedAt
        );
    }
}
