package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.MonitoringService;
import io.haechi.henesis.assignment.domain.arguments.ValueTransferEventArguments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class MonitoringServiceImpl implements MonitoringService {


    private final HenesisApiCallService henesisApiCallService;
    private int idx = 0;

    public MonitoringServiceImpl (HenesisApiCallService henesisApiCallService){
        this.henesisApiCallService = henesisApiCallService;
    }


    @Override
    @Transactional
    @Async
    @Scheduled(fixedRate = 500, initialDelay = 2000)
    public void getValueTransferEvents(String walletId, String masterWalletId, String transactionId, String updatedAtGte, String status, String size) {
        Long start_time = System.currentTimeMillis();

        henesisApiCallService.valueTransferEvent(ValueTransferEventArguments.builder()
                .walletId(walletId)
                .masterWalletId(masterWalletId)
                .transactionId(transactionId)
                .updatedAtGte(updatedAtGte)
                .status(status)
                .size(size)
                .build());
        Long end_time = System.currentTimeMillis();

        log.info("No."+idx+"\tRun-Time : "+(end_time-start_time)+"ms");
        idx++;
    }
}
