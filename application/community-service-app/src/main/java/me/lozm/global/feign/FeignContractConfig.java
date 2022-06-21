package me.lozm.global.feign;

import feign.Contract;
import org.springframework.context.annotation.Bean;

public class FeignContractConfig {
    @Bean
    public Contract contract() {
        return new Contract.Default();
    }
}
