package com.shopme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
//
//@EntityScan({"com.shopme.common"})
//@ComponentScan({"com.shopme.common", "com.shopme.security",
//"com.shopme.service", "com.shopme.repository"})
public class ShopmeFrontEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopmeFrontEndApplication.class, args);
    }
}
