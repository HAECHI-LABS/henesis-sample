package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.btc.BtcExchangeApplicationService;
import io.haechi.henesis.assignment.application.btc.dto.BtcTransferRequest;
import io.haechi.henesis.assignment.application.btc.dto.BtcTransferResponse;
import io.haechi.henesis.assignment.application.btc.dto.CreateDepositAddressRequest;
import io.haechi.henesis.assignment.application.btc.dto.CreateDepositAddressResponse;
import io.haechi.henesis.assignment.application.ethklay.EthExchangeApplicationService;
import io.haechi.henesis.assignment.application.ethklay.KlayExchangeApplicationService;
import io.haechi.henesis.assignment.application.ethklay.dto.*;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value="Ethan")
@RestController
@RequestMapping("exchange")
public class ExchangeController {

    private final EthExchangeApplicationService ethExchangeApplicationService;
    private final KlayExchangeApplicationService klayExchangeApplicationService;
    private final BtcExchangeApplicationService btcExchangeApplicationService;


    public ExchangeController(
            EthExchangeApplicationService ethExchangeApplicationService,
            KlayExchangeApplicationService klayExchangeApplicationService,
            BtcExchangeApplicationService btcExchangeApplicationService
    ) {
        this.ethExchangeApplicationService = ethExchangeApplicationService;
        this.klayExchangeApplicationService = klayExchangeApplicationService;
        this.btcExchangeApplicationService = btcExchangeApplicationService;
    }


    // 사용자 지갑 생성
    @PostMapping("eth/create-user-wallet/")
    @ResponseStatus(value = HttpStatus.OK)
    public CreateWalletResponse createEthWallet(@RequestBody CreateWalletRequest createWalletRequest) {
        return ethExchangeApplicationService.createUserWallet(createWalletRequest);
    }

    @PostMapping("klay/create-user-wallet/")
    @ResponseStatus(value = HttpStatus.OK)
    public CreateWalletResponse createKlayWallet(@RequestBody CreateWalletRequest createWalletRequest) {
        return klayExchangeApplicationService.createUserWallet(createWalletRequest);
    }

    @PostMapping("btc/create-deposit-address/")
    public CreateDepositAddressResponse createBtcDepositAddress(@RequestBody CreateDepositAddressRequest createDepositAddressRequest) {
        return btcExchangeApplicationService.createDepositAddress(createDepositAddressRequest);
    }

    // 출금하기
    @PostMapping("eth/transfer/")
    @ResponseStatus(value = HttpStatus.OK)
    public TransferResponse transferEth(@RequestBody TransferRequest transferRequest) {
        return ethExchangeApplicationService.transfer(transferRequest);
    }

    @PostMapping("klay/transfer/")
    @ResponseStatus(value = HttpStatus.OK)
    public TransferResponse transferKlay(@RequestBody TransferRequest transferRequest) {
        return klayExchangeApplicationService.transfer(transferRequest);
    }

    @PostMapping("btc/transfer/")
    @ResponseStatus(value = HttpStatus.OK)
    public BtcTransferResponse transferBtc(@RequestBody BtcTransferRequest btcTransferRequest) {
        return btcExchangeApplicationService.transfer(btcTransferRequest);
    }

    // 집금하기
    @PostMapping("eth/flush/")
    @ResponseStatus(value = HttpStatus.OK)
    public FlushResponse flushEth(@RequestBody FlushRequest flushRequest) {
        return ethExchangeApplicationService.flush();
    }

    // 집금하기
    @PostMapping("klay/flush/")
    @ResponseStatus(value = HttpStatus.OK)
    public FlushResponse flushKlay(@RequestBody FlushRequest flushRequest) {
        return klayExchangeApplicationService.flush();
    }


    @PostMapping("eth/updateAllWallet/")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateEthWalletList() {
        ethExchangeApplicationService.updateWalletList();

    }

    @PostMapping("klay/updateAllWallet/")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateKlayWalletList() {
        klayExchangeApplicationService.updateWalletList();
    }
}
