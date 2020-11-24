package io.haechi.henesis.assignment.domain.arguments;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBalanceArguments {
    private String symbol;
}
