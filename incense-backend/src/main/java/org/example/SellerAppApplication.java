package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class SellerAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(SellerAppApplication.class, args);
    }
}