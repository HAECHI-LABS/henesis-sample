package io.haechi.henesis.assignment.infra.btc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetTransfersJsonObject {

    @JsonProperty("pagination")
    private PaginationJsonObject pagination;
    @JsonProperty("results")
    private List<BtcTransferJsonObject> results;

}
