package io.haechi.henesis.assignment.config;

import io.haechi.henesis.assignment.application.dto.UserWalletDTO;
import io.haechi.henesis.assignment.domain.UserWallet;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
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
}
