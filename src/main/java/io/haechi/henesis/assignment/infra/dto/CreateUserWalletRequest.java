package io.haechi.henesis.assignment.infra.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserWalletRequest {
    private String name;
    private String passphrase;
}
