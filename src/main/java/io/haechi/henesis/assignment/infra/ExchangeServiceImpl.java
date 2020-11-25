package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.arguments.CreateUserArguments;
import io.haechi.henesis.assignment.domain.arguments.FlushArguments;
import io.haechi.henesis.assignment.domain.arguments.TransferArguments;
import io.haechi.henesis.assignment.domain.repository.FlushedTxRepository;
import io.haechi.henesis.assignment.domain.repository.UserWalletRepository;
import io.haechi.henesis.assignment.domain.util.Converter;
import io.haechi.henesis.assignment.infra.dto.UserWalletJsonObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final HenesisApiCallService henesisApiCallService;
    private final UserWalletRepository userWalletRepository;
    private final FlushedTxRepository flushedTxRepository;

    public ExchangeServiceImpl(HenesisApiCallService henesisApiCallService,
                               UserWalletRepository userWalletRepository,
                               FlushedTxRepository flushedTxRepository){
        this.henesisApiCallService = henesisApiCallService;
        this.userWalletRepository = userWalletRepository;
        this.flushedTxRepository = flushedTxRepository;
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


        if (convertedSpendableAmount.compareTo(convertedAmount)<0
        && convertedUserWalletBalance.compareTo(convertedAmount)<0){
            throw new IllegalStateException("NO MONEY!!!!");
        }else{

            Transaction transaction = henesisApiCallService.transfer(TransferArguments.builder()
                    .amount(amount)
                    .to(to)
                    .ticker(ticker)
                    .passphrase(passphrase)
                    .build()
            );

            // 잔고 차감
            userWallet.setWalletBalance(Converter.DoubleToHexString(convertedUserWalletBalance-convertedAmount));
            userWalletRepository.save(userWallet);

            return transaction;
        }

    }

    @Override
    @Transactional
    public Transaction flush(String ticker, String passphrase) {

        List<UserWalletJsonObject> userWallets = henesisApiCallService.getAllUserWallet().getResults();

        List<String> userWalletIds = userWallets.stream()
                .map(UserWalletJsonObject::getId)
                .collect(Collectors.toList());

        System.out.println("Get All User Wallet Id : "+userWalletIds);


        Transaction transaction = henesisApiCallService.flush(FlushArguments.builder()
                .ticker(ticker)
                .userWalletIds(userWalletIds)
                .passphrase(passphrase)
                .build()
        );

        FlushedTx flushedTx = new FlushedTx(transaction);

        flushedTxRepository.save(flushedTx);
        return transaction;
    }

}
