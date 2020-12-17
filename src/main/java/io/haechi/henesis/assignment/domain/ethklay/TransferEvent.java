package io.haechi.henesis.assignment.domain.ethklay;


import io.haechi.henesis.assignment.domain.Pagination;
import io.haechi.henesis.assignment.domain.btc.BtcTransaction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferEvent {
    private Pagination pagination;
    private List<Transaction> results;

    private TransferEvent(
            Pagination pagination,
            List<Transaction> results
    ){
        this.pagination =pagination;
        this.results = results;
    }

    public static TransferEvent of(
            Pagination pagination,
            List<Transaction> results
    ){
        return new TransferEvent(
                pagination,
                results
        );
    }
}