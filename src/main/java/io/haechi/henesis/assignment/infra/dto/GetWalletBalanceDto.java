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
public class GetWalletBalanceDto {
    private String coinType;
    private String amount;
    private String spendableAmount;
    private String name;
    private String symbol;
}
