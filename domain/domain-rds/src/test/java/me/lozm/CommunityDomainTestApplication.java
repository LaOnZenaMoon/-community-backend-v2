package me.lozm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class CommunityDomainTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityDomainTestApplication.class, args);
    }

}
