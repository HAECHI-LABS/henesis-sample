package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignedMultiSigPayloadDTO {

    @JsonProperty("signature")
    private String signature;
    @JsonProperty("multiSigPayload")
    private MultiSigPayloadDTO multiSigPayloadDTO;

}
