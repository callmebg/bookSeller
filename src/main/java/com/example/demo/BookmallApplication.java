package com.example.demo;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class BookmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookmallApplication.class, args);
    }

}
