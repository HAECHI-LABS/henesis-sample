package io.haechi.henesis.assignment.domain.arguments;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserArguments {
    private String name;
    private String passphrase;
}
