package io.haechi.henesis.assignment.domain.btc;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountKey {
    private String address;
    private String pub;
    private String keyFile;
}
