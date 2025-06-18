package com.example.Ask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.example.Ask",
    "com.example.config",
    "com.example.Service",
    "com.example.Ask.Controllers",
    "com.example.Ask.Entities",
    "com.example.Ask.Repositiries"
})
public class AskApplication {
    public static void main(String[] args) {
        SpringApplication.run(AskApplication.class, args);
    }
}
