package io.haechi.henesis.assignment.infra.ethklay.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValueTransferEventResponse {
    private Long id;
    private String from;
    private String to;
    private String amount;
    private String blockchain;
    private String status;
    private String coinSymbol;
    private String transferType;
    private String hash;
    private String createdAt;
    private String updatedAt;

    public String getId() {
        return Long.toString(this.id);
    }
}
