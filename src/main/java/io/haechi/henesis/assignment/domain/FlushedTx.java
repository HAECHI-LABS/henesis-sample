package io.haechi.henesis.assignment.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushedTx {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String txId;
    private String blockchain;
    private String status;
    private String createdAt;

    public FlushedTx(Transaction transaction) {
        this.txId = transaction.getTxId();
        this.blockchain = transaction.getBlockchain();
        this.status = transaction.getStatus();
        this.createdAt = transaction.getCreatedAt();
    }
}
