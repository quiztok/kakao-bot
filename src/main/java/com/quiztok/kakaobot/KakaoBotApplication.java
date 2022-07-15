package com.quiztok.kakaobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@PropertySource("classpath:application-${profile:dev}.properties")
public class KakaoBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakaoBotApplication.class, args);
    }

}
