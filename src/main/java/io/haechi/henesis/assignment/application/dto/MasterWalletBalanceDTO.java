package io.haechi.henesis.assignment.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MasterWalletBalanceDTO {
    private String coinId;
    private String coinType;
    private String amount;
    private String decimals;
    private String spendableAmount;
    private String aggregatedAmount;
    private String name;
    private String symbol;

}
