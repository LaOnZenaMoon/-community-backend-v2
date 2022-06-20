package me.lozm.global.password;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class PasswordConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void clientSecretTest() {
        // Given
        final String plainSecretKey = "laonzenamoon_client_secret";

        // When
        String encode = passwordEncoder.encode(plainSecretKey);

        // Then
        System.out.println("encode = " + encode);
    }


}