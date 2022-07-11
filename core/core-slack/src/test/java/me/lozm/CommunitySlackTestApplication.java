package me.lozm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CommunitySlackTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunitySlackTestApplication.class, args);
    }

}
