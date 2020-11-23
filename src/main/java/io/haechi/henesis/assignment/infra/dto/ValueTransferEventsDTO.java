package io.haechi.henesis.assignment.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValueTransferEventsDTO {

    @JsonProperty("pagination")
    private PaginationDTO pagination;
    @JsonProperty("results")
    private List<ResultsDTO> results;

}
