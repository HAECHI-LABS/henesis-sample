package io.haechi.henesis.assignment.infra.btc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountKeyJsonObject {
    @JsonProperty("address")
    private String address;
    @JsonProperty("pub")
    private String pub;
    @JsonProperty("keyFile")
    private String keyFile;
}