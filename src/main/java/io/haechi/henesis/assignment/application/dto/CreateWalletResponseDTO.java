package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateWalletResponseDTO {

    private String id;
    private String name;
    private String address;
    private String blockchain;
    private String status;
    private String error;
    private String transactionId;
    private String createdAt;
    private String updatedAt;
}
