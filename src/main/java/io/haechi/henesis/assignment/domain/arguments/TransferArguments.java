package io.haechi.henesis.assignment.domain.arguments;

import lombok.*;

import java.math.BigInteger;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TransferArguments {
    private String ticker;
    private String to;
    private String amount;
    private String passphrase;
}
