package me.lozm.slack.client;

import me.lozm.slack.vo.SlackMessageVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slack-client", url = "${feign.slack-client.url}")
public interface SlackClient {

    @PostMapping
    String sendMessage(@RequestBody SlackMessageVo slackMessageVo);

}
