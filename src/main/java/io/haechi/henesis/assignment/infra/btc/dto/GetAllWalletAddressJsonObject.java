package io.haechi.henesis.assignment.infra.btc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAllWalletAddressJsonObject {

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private String address;
    @JsonProperty("orgId")
    private String orgId;
    @JsonProperty("accountKey")
    private AccountKeyJsonObject accountKeyJsonObject;
    @JsonProperty("encrytionKey")
    private String encryptionKey;
    @JsonProperty("status")
    private String status;
    @JsonProperty("whitelistActivated")
    private boolean whitelistActivated;
    @JsonProperty("createdAt")
    private String createdAt;
}
