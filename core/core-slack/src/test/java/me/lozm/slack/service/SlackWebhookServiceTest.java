package me.lozm.slack.service;

import lombok.extern.slf4j.Slf4j;
import me.lozm.slack.vo.SlackMessageVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("local")
@SpringBootTest
class SlackWebhookServiceTest {

    @Autowired
    private SlackWebhookService slackWebhookService;


    @Test
    void test() {
        // Given
        final String message = "Hello world!";

        // When
        slackWebhookService.send(new SlackMessageVo(message));

        // Then
    }

}