package io.haechi.henesis.assignment.infra.ethklay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAllUserWalletJsonObject {
    @JsonProperty("pagination")
    private PaginationJsonObject pagination;
    @JsonProperty("results")
    private List<UserWalletJsonObject> results;

}