package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.application.dto.FlushResponseDTO;
import io.haechi.henesis.assignment.application.dto.TransferResponseDTO;
import io.haechi.henesis.assignment.domain.Transaction;
import io.haechi.henesis.assignment.application.dto.CreateWalletResponseDTO;
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
    public PropertyMap<UserWallet, CreateWalletResponseDTO> userWalletUserWalletDTOPropertyMap(){
        return new PropertyMap<UserWallet, CreateWalletResponseDTO>() {
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
    public PropertyMap<Transaction, TransferResponseDTO> transactionTransactionDTOPropertyMap(){
        return new PropertyMap<Transaction, TransferResponseDTO>() {
            @Override
            protected void configure() {
                map().setTxId(source.getTxId());
                map().setBlockchain(source.getBlockchain());
                map().setStatus(source.getStatus());
            }
        };
    }

    @Bean
    public PropertyMap<Transaction, FlushResponseDTO> transactionFlushResponseDTOPropertyMap(){
        return new PropertyMap<Transaction, FlushResponseDTO>() {
            @Override
            protected void configure() {
                map().setTxId(source.getTxId());
                map().setBlockchain(source.getBlockchain());
                map().setStatus(source.getStatus());
            }
        };
    }

    @Bean
    public ModelMapper modelMapper(
            PropertyMap<UserWallet, CreateWalletResponseDTO> userWalletUserWalletDTOPropertyMap,
            PropertyMap<Transaction, TransferResponseDTO> transactionTransactionDTOPropertyMap,
            PropertyMap<Transaction, FlushResponseDTO> transactionFlushResponseDTOPropertyMap
    ){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(userWalletUserWalletDTOPropertyMap);
        modelMapper.addMappings(transactionTransactionDTOPropertyMap);
        modelMapper.addMappings(transactionFlushResponseDTOPropertyMap);
        return modelMapper;
    }
}
