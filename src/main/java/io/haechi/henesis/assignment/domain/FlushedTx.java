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

    private FlushedTx(
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

    public static FlushedTx of(
            String txId,
            String blockchain,
            String status,
            String createdAt
    ){
        return new FlushedTx(
                txId,
                blockchain,
                status,
                createdAt
        );
    }



}
