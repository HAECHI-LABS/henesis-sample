package io.haechi.henesis.assignment.domain;


import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {
    private String txId;
    private String blockchain;
    private String status;
    private String createdAt;

}
