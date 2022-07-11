package me.lozm.slack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lozm.slack.client.SlackClient;
import me.lozm.slack.vo.SlackMessageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackWebhookService {

    private final Environment environment;
    private final SlackClient slackClient;


    public void send(SlackMessageVo slackMessageVo) {
        final String slackClientUrl = environment.getProperty("feign.slack-client.url");
        if (StringUtils.isEmpty(slackClientUrl) || slackClientUrl.equals("none")) {
            return;
        }

        try {
            String result = slackClient.sendMessage(slackMessageVo);
            log.debug("Slack Webhook Send Message: {}", result);
        } catch (Exception e) {
            log.error("Slack Webhook Send Message Error: {}", e.getMessage());
            e.printStackTrace();
        }
    }

}
