package me.lozm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CommunityAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityAuthApplication.class, args);
    }

}
