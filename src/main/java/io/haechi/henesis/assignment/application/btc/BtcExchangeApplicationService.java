package io.haechi.henesis.assignment.application.btc;

import io.haechi.henesis.assignment.application.btc.dto.BtcTransferRequest;
import io.haechi.henesis.assignment.application.btc.dto.BtcTransferResponse;
import io.haechi.henesis.assignment.application.btc.dto.CreateDepositAddressRequest;
import io.haechi.henesis.assignment.application.btc.dto.CreateDepositAddressResponse;
import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.Transaction;
import io.haechi.henesis.assignment.domain.btc.BtcHenesisWalletClient;
import io.haechi.henesis.assignment.domain.btc.DepositAddress;
import io.haechi.henesis.assignment.domain.btc.DepositAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class BtcExchangeApplicationService {

    private final BtcHenesisWalletClient btcHenesisWalletClient;
    private final DepositAddressRepository depositAddressRepository;
    private final String walletId;

    public BtcExchangeApplicationService(BtcHenesisWalletClient btcHenesisWalletClient,
                                         DepositAddressRepository depositAddressRepository,
                                         @Qualifier("btcWalletId") String walletId) {
        this.btcHenesisWalletClient = btcHenesisWalletClient;
        this.depositAddressRepository = depositAddressRepository;
        this.walletId = walletId;
    }


    @Transactional
    public CreateDepositAddressResponse createDepositAddress(CreateDepositAddressRequest request) {

        DepositAddress depositAddress = btcHenesisWalletClient.createDepositAddress(request.getName());
        depositAddressRepository.save(depositAddress);
        log.info(String.format("Creating Deposit Address (%s)", request.getName()));

        return CreateDepositAddressResponse.of(
                depositAddress.getName(),
                depositAddress.getAddress()
        );
    }

    @Transactional
    public BtcTransferResponse transfer(BtcTransferRequest request) {

        // 보내는 사용자 지갑 조회 for check and update User Wallet Balance
        DepositAddress depositAddress = depositAddressRepository.findByDepositAddressId(request.getDepositAddressId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can Not Found Deposit Address %s", request.getDepositAddressId())));


        // 지갑 상태가 ACTIVE 가 아닌 경우 에러 발생
        if (!btcHenesisWalletClient.getWalletInfo().getStatus().contains("ACTIVE")) {
            throw new IllegalStateException("Wallet is NOT ACTIVE Status");
        }

        // 해당 사용자 지갑이 속한 마스터지갑 잔고 조회
        Amount spendableAmount = btcHenesisWalletClient.getWalletBalance();
        Amount balance = depositAddress.getBalance();
        Amount amount = request.getAmount();

        // 출금 가능 여부 판단
        if (balance.compareTo(request.getAmount()) < 0) {
            throw new IllegalStateException(String.format("Not Enough Balance in %s..!",depositAddress.getName()));
        }
        if (spendableAmount.compareTo(balance) < 0) {
            throw new IllegalStateException("Not Enough Wallet Balance..!");
        }

        // 마스터 지갑에서 코인/토큰 전송하기 API 호출
        btcHenesisWalletClient.transfer(
                request.getAmount(),
                request.getTo()
        );

        // 잔고 차감
        balance.subtract(amount);
        depositAddressRepository.save(depositAddress);
        log.info(String.format("Deposit Address(%s) Balance : %s", depositAddress.getName(), balance.toHexString()));

        return BtcTransferResponse.of(
                depositAddress.getName(),
                request.getAmount(),
                balance
        );
    }
}
