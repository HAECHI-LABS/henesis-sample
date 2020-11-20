package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaginationDTO {
    @JsonProperty("nextUrl")
    private String nextUrl;
    @JsonProperty("previousUrl")
    private String previousUrl;
    @JsonProperty("totalCount")
    private int totalCount;
}
