package io.haechi.henesis.assignment.application.ethklay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushRequest {
    @JsonProperty("count")
    private Integer count;
}
