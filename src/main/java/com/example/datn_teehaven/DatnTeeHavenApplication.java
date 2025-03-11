package com.example.datn_teehaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DatnTeeHavenApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatnTeeHavenApplication.class, args);
    }

}
