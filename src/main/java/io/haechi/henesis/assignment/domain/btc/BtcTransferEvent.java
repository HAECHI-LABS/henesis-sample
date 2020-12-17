package io.haechi.henesis.assignment.domain.btc;

import io.haechi.henesis.assignment.domain.Pagination;
import lombok.*;

import java.util.List;



@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransferEvent {
    private Pagination pagination;
    private List<BtcTransaction> results;

    private BtcTransferEvent(
            Pagination pagination,
            List<BtcTransaction> results
    ){
        this.pagination =pagination;
        this.results = results;
    }

    public static BtcTransferEvent of(
            Pagination pagination,
            List<BtcTransaction> results
    ){
        return new BtcTransferEvent(
                pagination,
                results
        );
    }

    public boolean canStop(BtcTransferEvent btcTransferEvent){
        return btcTransferEvent.getPagination().getNextUrl() == null;
    }
}
