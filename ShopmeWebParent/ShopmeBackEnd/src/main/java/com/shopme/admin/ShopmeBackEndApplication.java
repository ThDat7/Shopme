package com.shopme.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties

@EntityScan({"com.shopme.common", "com.shopme.admin"})
@ComponentScan({"com.shopme.common", "com.shopme.admin"})

public class ShopmeBackEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopmeBackEndApplication.class, args);
    }
}
