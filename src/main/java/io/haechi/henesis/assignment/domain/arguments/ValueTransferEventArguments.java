package io.haechi.henesis.assignment.domain.arguments;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValueTransferEventArguments {
    private String walletId;
    private String masterWalletId;
    private String transactionId;
    private String updatedAtGte;
    private String status;
    private String size;
}
