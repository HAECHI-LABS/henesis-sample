package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.application.dto.MasterWalletBalanceDTO;
import io.haechi.henesis.assignment.application.dto.TransactionDTO;
import io.haechi.henesis.assignment.application.dto.UserWalletDTO;
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
    public PropertyMap<UserWallet, UserWalletDTO> userWalletUserWalletDTOPropertyMap(){
        return new PropertyMap<UserWallet, UserWalletDTO>() {
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
    public PropertyMap<FlushedTx, TransactionDTO> flushedTxTransactionDTOPropertyMap(){
        return new PropertyMap<FlushedTx, TransactionDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getTxId());
                map().setStatus(source.getStatus());
                map().setBlockchain(source.getBlockchain());
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
    public ModelMapper modelMapper(
            PropertyMap<UserWallet,UserWalletDTO> userWalletUserWalletDTOPropertyMap,
            PropertyMap<MasterWalletBalance, MasterWalletBalanceDTO> masterWalletBalanceMasterWalletBalanceDTOPropertyMap,
            PropertyMap<FlushedTx, TransactionDTO> flushedTxTransactionDTOPropertyMap
    ){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(userWalletUserWalletDTOPropertyMap);
        modelMapper.addMappings(masterWalletBalanceMasterWalletBalanceDTOPropertyMap);
        modelMapper.addMappings(flushedTxTransactionDTOPropertyMap);
        return modelMapper;
    }
}
