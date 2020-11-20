package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultiSigPayloadDTO {

    @JsonProperty("value")
    private String value;
    @JsonProperty("walletAddress")
    private String walletAddress;
    @JsonProperty("toAddress")
    private String toAddress;
    @JsonProperty("walletNonce")
    private String walletNonce;
    @JsonProperty("hexData")
    private String hexData;




}
