package io.haechi.henesis.assignment.domain;

import lombok.*;

import java.math.BigInteger;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MasterWalletBalance {
    private String coinId;
    private String coinType;
    private BigInteger amount;
    private Integer decimals;
    private BigInteger spendableAmount;
    private BigInteger aggregatedAmount;
    private String name;
    private String symbol;
}
