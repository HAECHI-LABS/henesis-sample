package io.haechi.henesis.assignment.domain.btc;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransaction {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String walletId;
    private String feeAmount;
    private String receivedAt;
    private String sendTo;
    private String type;
    private String status;

    private String transactionId;
    private String transactionHash;
    private String createdAt;
    private String updatedAt;
}
