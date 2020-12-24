package io.haechi.henesis.assignment.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateDepositAddressDto {
    private String id;
    private String name;
    private String address;
    private String blockchain;
    private String status;
    private String createdAt;
}
