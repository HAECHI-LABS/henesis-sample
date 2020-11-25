package io.haechi.henesis.assignment.domain.arguments;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushArguments {
    private String ticker;
    private List<String> userWalletIds;
    private String passphrase;
}
