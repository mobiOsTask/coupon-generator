package org.example;

import jakarta.annotation.PostConstruct;
import org.example.config.RsaKeyProperties;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class);
        logger.info("Main Class Executing");
    }


    @PostConstruct
    public void init(){
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Colombo"));
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}