package com.shopme.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfigUtils {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
