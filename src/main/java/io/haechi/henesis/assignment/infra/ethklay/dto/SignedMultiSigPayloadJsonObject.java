package io.haechi.henesis.assignment.infra.ethklay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignedMultiSigPayloadJsonObject {

    @JsonProperty("signature")
    private String signature;
    @JsonProperty("multiSigPayload")
    private MultiSigPayloadJsonObject multiSigPayloadJsonObject;

}
