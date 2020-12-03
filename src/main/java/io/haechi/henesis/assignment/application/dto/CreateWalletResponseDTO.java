package io.haechi.henesis.assignment.application.dto;

import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateWalletResponseDTO {

    private String id;
    private String name;
    private String address;
    private String blockchain;
    private String status;
    private String createdAt;
    private String updatedAt;


    private CreateWalletResponseDTO (
            String id,
            String name,
            String address,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ){
        this.id =id;
        this.name =name;
        this.address = address;
        this.blockchain = blockchain;
        this.status =status;
        this.createdAt =createdAt;
        this.updatedAt =updatedAt;
    }

    public static CreateWalletResponseDTO of(
            String id,
            String name,
            String address,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ){
        return new CreateWalletResponseDTO(
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
