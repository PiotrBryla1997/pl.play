package com.example.playdocker;

import com.google.api.client.util.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    @Value("$(environment)")
    public static String environment;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
