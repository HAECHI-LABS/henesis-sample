package io.haechi.henesis.assignment.domain.ethklay;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushedTransaction {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String transactionId;
    private String blockchain;
    private String status;
    private String createdAt;
    private String updatedAt;

    private FlushedTransaction(
            String transactionId,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ) {
        this.transactionId = transactionId;
        this.blockchain = blockchain;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FlushedTransaction of(
            String transactionId,
            String blockchain,
            String status,
            String createdAt,
            String updatedAt
    ) {
        return new FlushedTransaction(
                transactionId,
                blockchain,
                status,
                createdAt,
                updatedAt
        );
    }


}
