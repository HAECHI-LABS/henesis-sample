package io.haechi.henesis.assignment.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MasterWalletBalance {
    private String coinId;
    private String coinType;
    private String amount;
    private String decimals;
    private String spendableAmount;
    private String aggregatedAmount;
    private String name;
    private String symbol;
}
