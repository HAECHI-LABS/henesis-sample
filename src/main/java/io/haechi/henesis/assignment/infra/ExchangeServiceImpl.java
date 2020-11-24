package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.arguments.CreateUserArguments;
import io.haechi.henesis.assignment.domain.arguments.TransferArguments;
import io.haechi.henesis.assignment.domain.repository.UserWalletRepository;
import io.haechi.henesis.assignment.domain.util.Converter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Optional;

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
    public UserWallet createUserWallet(CreateUserArguments request){
        UserWallet userWallet = henesisApiCallService.createUserWallet(request);
        userWalletRepository.save(userWallet);

        return userWallet;
    }

    @Override
    @Transactional
    public Transaction transfer(String userWalletId,
                                String amount,
                                String to,
                                String ticker,
                                String passphrase) {



        Optional<MasterWalletBalance> masterWalletBalance = henesisApiCallService.getMasterWalletBalance(ticker);
        UserWallet userWallet =  userWalletRepository.getUserWalletByWalletId(userWalletId);

        Double convertedAmount = Converter.hexStringToDouble(amount);
        Double convertedSpendableAmount = Converter.hexStringToDouble(masterWalletBalance.get().getSpendableAmount());
        Double convertedUserWalletBalance = Converter.hexStringToDouble(userWallet.getWalletBalance());

        System.out.println("userWallet Name : "+userWallet.getWalletName());
        System.out.println("userWallet Balance : "+userWallet.getWalletBalance());
        System.out.println("Converted userWallet Balance : "+convertedUserWalletBalance);


        System.out.println("Amount : "+ amount);
        System.out.println("Converted Amount : "+ convertedAmount);

        System.out.println("MasterWallet Spendable Balance : "+masterWalletBalance.get().getSpendableAmount());
        System.out.println("Converted MasterWallet Spendable Balance : "+convertedSpendableAmount);

        if (convertedSpendableAmount.compareTo(convertedAmount)<0
        && convertedUserWalletBalance.compareTo(convertedAmount)<0){
            throw new IllegalStateException("NO MONEY!!!!");
        }else{
            System.out.println("Before Wallet Balance : "+Converter.hexStringToDouble(userWallet.getWalletBalance()));

            userWallet.setWalletBalance(Converter.DoubleToHexString(convertedUserWalletBalance-convertedAmount));

            System.out.println("After Wallet Balance : "+Converter.hexStringToDouble(userWallet.getWalletBalance()));

            userWalletRepository.save(userWallet);

            TransferArguments transferArguments = TransferArguments.builder()
                    .amount(amount)
                    .to(to)
                    .ticker(ticker)
                    .passphrase(passphrase)
                    .build();

            return henesisApiCallService.transfer(transferArguments);
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
