package io.haechi.henesis.assignment.application.dto;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWalletDTO {

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
