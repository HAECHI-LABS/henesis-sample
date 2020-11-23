package io.haechi.henesis.assignment.domain;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferResponse {
    private String id;
    private String status;
    private String blockchain;
}
