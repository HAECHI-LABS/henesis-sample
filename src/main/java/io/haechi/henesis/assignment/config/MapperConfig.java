package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.application.dto.MasterWalletBalanceDTO;
import io.haechi.henesis.assignment.application.dto.TransferCoinDTO;
import io.haechi.henesis.assignment.domain.Transaction;
import io.haechi.henesis.assignment.infra.dto.TransactionDTO;
import io.haechi.henesis.assignment.application.dto.CreateWalletDTO;
import io.haechi.henesis.assignment.domain.FlushedTx;
import io.haechi.henesis.assignment.domain.MasterWalletBalance;
import io.haechi.henesis.assignment.domain.UserWallet;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    private final ModelMapper modelMapper = new ModelMapper();


    @Bean
    public PropertyMap<UserWallet, CreateWalletDTO> userWalletUserWalletDTOPropertyMap(){
        return new PropertyMap<UserWallet, CreateWalletDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getWalletId());
                map().setName(source.getWalletName());
                map().setAddress(source.getWalletAddress());
                map().setBlockchain(source.getBlockchain());
                map().setStatus(source.getStatus());
                map().setCreatedAt(source.getCreatedAt());
                map().setUpdatedAt(source.getUpdatedAt());
            }
        };
    }



    @Bean
    public PropertyMap<MasterWalletBalance, MasterWalletBalanceDTO> masterWalletBalanceMasterWalletBalanceDTOPropertyMap(){
        return new PropertyMap<MasterWalletBalance, MasterWalletBalanceDTO>() {
            @Override
            protected void configure() {
                map().setCoinId(source.getCoinId());
                map().setCoinType(source.getCoinType());
                map().setAmount(source.getAmount());
                map().setDecimals(source.getDecimals());
                map().setSpendableAmount(source.getSpendableAmount());
                map().setAggregatedAmount(source.getAggregatedAmount());
                map().setName(source.getName());
                map().setSymbol(source.getSymbol());
            }
        };
    }

    @Bean
    public PropertyMap<Transaction, TransferCoinDTO> transactionTransactionDTOPropertyMap(){
        return new PropertyMap<Transaction, TransferCoinDTO>() {
            @Override
            protected void configure() {
                map().setTxId(source.getId());
                map().setBlockchain(source.getBlockchain());
                map().setStatus(source.getStatus());
            }
        };
    }

    @Bean
    public ModelMapper modelMapper(
            PropertyMap<UserWallet, CreateWalletDTO> userWalletUserWalletDTOPropertyMap,
            PropertyMap<MasterWalletBalance, MasterWalletBalanceDTO> masterWalletBalanceMasterWalletBalanceDTOPropertyMap,
            PropertyMap<Transaction, TransferCoinDTO> transactionTransactionDTOPropertyMap
    ){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(userWalletUserWalletDTOPropertyMap);
        modelMapper.addMappings(masterWalletBalanceMasterWalletBalanceDTOPropertyMap);
        modelMapper.addMappings(transactionTransactionDTOPropertyMap);
        return modelMapper;
    }
}
