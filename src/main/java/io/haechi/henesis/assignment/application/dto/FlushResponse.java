package io.haechi.henesis.assignment.application.dto;

import io.haechi.henesis.assignment.domain.Transfer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushResponse {
    private Long id;

    public FlushResponse(Transfer transfer) {
        this.id = transfer.getId();
    }
}
