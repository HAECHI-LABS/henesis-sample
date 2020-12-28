package io.haechi.henesis.assignment.infra.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthKlayTransferDto {
    private String id;
    private String blockchain;
    private String sender;
    private String hash;
    private String coinSymbol;
    private String error;
    private String status;
    private String keyId;
    private String createdAt;
}
