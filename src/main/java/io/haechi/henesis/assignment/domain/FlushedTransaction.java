package io.haechi.henesis.assignment.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushedTransaction {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String txId;
    private String blockchain;
    private String status;
    private String createdAt;

    private FlushedTransaction(
            String txId,
            String blockchain,
            String status,
            String createdAt
    ){
        this.txId=txId;
        this.blockchain=blockchain;
        this.status=status;
        this.createdAt=createdAt;
    }

    public static FlushedTransaction of(
            String txId,
            String blockchain,
            String status,
            String createdAt
    ){
        return new FlushedTransaction(
                txId,
                blockchain,
                status,
                createdAt
        );
    }



}
