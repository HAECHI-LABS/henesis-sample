package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("blockchain")
    private String blockchain;
    @JsonProperty("sender")
    private String sender;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("error")
    private String error;
    @JsonProperty("status")
    private String status;
    @JsonProperty("keyId")
    private String keyId;
    @JsonProperty("createdAt")
    private String createdAt;


    @JsonProperty("signedMultiSigPayload")
    private SignedMultiSigPayloadDTO signedMultiSigPayloadDTO;
    @JsonProperty("rawTransaction")
    private RawTransactionDTO rawTransactionDTO;







}
