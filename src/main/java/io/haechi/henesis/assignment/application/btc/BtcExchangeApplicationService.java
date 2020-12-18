package io.haechi.henesis.assignment.application.btc;

import io.haechi.henesis.assignment.application.btc.dto.BtcTransferRequest;
import io.haechi.henesis.assignment.application.btc.dto.BtcTransferResponse;
import io.haechi.henesis.assignment.application.btc.dto.CreateDepositAddressRequest;
import io.haechi.henesis.assignment.application.btc.dto.CreateDepositAddressResponse;
import io.haechi.henesis.assignment.domain.btc.BtcWalletService;
import io.haechi.henesis.assignment.domain.btc.DepositAddress;
import io.haechi.henesis.assignment.domain.btc.DepositAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class BtcExchangeApplicationService {

    private final BtcWalletService btcWalletService;
    private final DepositAddressRepository depositAddressRepository;

    public BtcExchangeApplicationService(
            @Qualifier("btcHenesisWalletService") BtcWalletService btcWalletService,
            DepositAddressRepository depositAddressRepository) {
        this.btcWalletService = btcWalletService;
        this.depositAddressRepository = depositAddressRepository;
    }


    @Transactional
    public CreateDepositAddressResponse createDepositAddress(CreateDepositAddressRequest request) {

        DepositAddress depositAddress = btcWalletService.createDepositAddress(request.getName());
        depositAddressRepository.save(depositAddress);
        log.info(String.format("Creating Deposit Address (%s)", request.getName()));

        return CreateDepositAddressResponse.of(request.getName());
    }

    @Transactional
    public BtcTransferResponse transfer(BtcTransferRequest request) {

        // 보내는 주소 조회 for check and update Deposit Address Balance
        DepositAddress depositAddress = depositAddressRepository.findByAddress(request.getFrom())
                .orElseThrow(() -> new IllegalArgumentException(String.format("ERROR : Can Not Found Deposit Address. (%s)", request.getFrom())));

        depositAddress.withdrawBy(
                request.getAmount(),
                btcWalletService.getEstimatedFee(),
                btcWalletService.getWalletBalance()
        );
        depositAddressRepository.save(depositAddress);
        log.info(String.format("Withdraw Balance..! (%s)", depositAddress.getName()));

        btcWalletService.transfer(request.getAmount(), request.getTo());
        log.info(String.format("Transfer Requested..! (%s)", depositAddress.getName()));

        return BtcTransferResponse.of(
                depositAddress.getName(),
                request.getAmount().toHexString()
        );
    }
}
