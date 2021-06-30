package com.example.schoolmanagerrestclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SchoolManagerRestClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolManagerRestClientApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
