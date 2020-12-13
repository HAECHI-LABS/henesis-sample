package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.ethKlayApplication.EthExchangeApplicationService;
import io.haechi.henesis.assignment.ethKlayApplication.ExchangeApplicationService;
import io.haechi.henesis.assignment.ethKlayApplication.KlayExchangeApplicationService;
import io.haechi.henesis.assignment.ethKlayApplication.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange")
public class ExchangeController {

    private final EthExchangeApplicationService ethExchangeApplicationService;
    private final KlayExchangeApplicationService klayExchangeApplicationService;


    public ExchangeController(
            EthExchangeApplicationService ethExchangeApplicationService,
            KlayExchangeApplicationService klayExchangeApplicationService
    ) {
        this.ethExchangeApplicationService = ethExchangeApplicationService;
        this.klayExchangeApplicationService = klayExchangeApplicationService;
    }


    // 사용자 지갑 생성
    @PostMapping("eth/create-user-wallet/")
    @ResponseStatus(value = HttpStatus.OK)
    public CreateWalletResponseDTO createEthWallet(@RequestBody CreateWalletRequestDTO createWalletRequestDTO) {
        return ethExchangeApplicationService.createUserWallet(createWalletRequestDTO);
    }

    // 사용자 지갑 생성
    @PostMapping("klay/create-user-wallet/")
    @ResponseStatus(value = HttpStatus.OK)
    public CreateWalletResponseDTO createKlayWallet(@RequestBody CreateWalletRequestDTO createWalletRequestDTO) {
        return klayExchangeApplicationService.createUserWallet(createWalletRequestDTO);
    }

    // 출금하기
    @PostMapping("eth/transfer/")
    @ResponseStatus(value = HttpStatus.OK)
    public TransferResponseDTO transferEth(@RequestBody TransferRequestDTO transferRequestDTO) {
        return ethExchangeApplicationService.transfer(transferRequestDTO);
    }
    // 출금하기
    @PostMapping("klay/transfer/")
    @ResponseStatus(value = HttpStatus.OK)
    public TransferResponseDTO transferKlay(@RequestBody TransferRequestDTO transferRequestDTO) {
        return klayExchangeApplicationService.transfer(transferRequestDTO);
    }


    // 집금하기
    @PostMapping("eth/flush/")
    @ResponseStatus(value = HttpStatus.OK)
    public FlushResponseDTO flushEth(@RequestBody FlushRequestDTO flushRequestDTO) {
        return ethExchangeApplicationService.flush(flushRequestDTO);
    }

    // 집금하기
    @PostMapping("klay/flush/")
    @ResponseStatus(value = HttpStatus.OK)
    public FlushResponseDTO flushKlay(@RequestBody FlushRequestDTO flushRequestDTO) {
        return klayExchangeApplicationService.flush(flushRequestDTO);
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
