package io.haechi.henesis.assignment.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushedTx {
    private String id;
    private String txId;
    private String blockchain;
    private String status;
}
