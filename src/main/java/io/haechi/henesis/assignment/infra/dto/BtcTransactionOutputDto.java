package io.haechi.henesis.assignment.infra.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransactionOutputDto {
    private int outputIndex;
    private String transactionId;
    private String address;
    private String amount;
    private boolean isChange;
    private String scriptPubKey;
}
