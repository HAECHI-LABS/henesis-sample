package io.haechi.henesis.assignment.infra.btc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaginationJsonObject {
    @JsonProperty("nextUrl")
    private String nextUrl;
    @JsonProperty("previousUrl")
    private String previousUrl;
    @JsonProperty("totalCount")
    private int totalCount;
}
