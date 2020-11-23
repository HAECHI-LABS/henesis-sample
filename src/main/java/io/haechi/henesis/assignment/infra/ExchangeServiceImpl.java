package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.repository.UserWalletRepository;
import io.haechi.henesis.assignment.application.dto.CreateUserWalletDTO;
import io.haechi.henesis.assignment.application.dto.TransferDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final HenesisApiCallService henesisApiCallService;
    private final UserWalletRepository userWalletRepository;

    public ExchangeServiceImpl(HenesisApiCallService henesisApiCallService,
                               UserWalletRepository userWalletRepository){
        this.henesisApiCallService = henesisApiCallService;
        this.userWalletRepository = userWalletRepository;
    }



    @Override
    @Transactional
    public UserWallet createUserWallet(CreateUserWalletDTO createUserWalletDTO){
        UserWallet userWallet = henesisApiCallService.createUserWallet(createUserWalletDTO);
        userWalletRepository.save(userWallet);

        return userWallet;
    }

    @Override
    @Transactional
    public Transaction transferCoin(TransferDTO transferDTO) {

        Optional<MasterWalletBalance> masterWalletBalance = henesisApiCallService.getMasterWalletBalance(transferDTO.getTicker());
        System.out.println("test : "+masterWalletBalance.get());
        UserWallet userWallet =  userWalletRepository.getUserWalletByWalletId(transferDTO.getUserWalletId());

        if (masterWalletBalance.get().getSpendableAmount().compareTo(transferDTO.getAmount())<0
        && userWallet.getWalletBalance().compareTo(transferDTO.getAmount())<0){
            System.out.println("Noooo");
            throw new IllegalStateException(String.format("잔고 부족 ㅎㅎ.."));
        }else{
            userWallet.setWalletBalance(userWallet.getWalletBalance().subtract(transferDTO.getAmount()));
            userWalletRepository.save(userWallet);
            Transaction transaction = henesisApiCallService.transfer(transferDTO);
            return transaction;
        }

    }


    @Override
    public UserWallet findUserWalletByWalletId(String walletId) {
        return null;
    }

    @Override
    public MasterWalletBalance findMasterWalletBalanceById(String masterWalletId) {
        return null;
    }

    @Override
    public FlushedTx findFlushedTxByTxId(String txId) {
        return null;
    }


}
