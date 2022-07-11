package me.lozm.global;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.RequiredArgsConstructor;
import me.lozm.slack.service.SlackWebhookService;
import me.lozm.slack.vo.SlackMessageVo;

@RequiredArgsConstructor
public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private final SlackWebhookService slackWebhookService;


    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!eventObject.getLevel().isGreaterOrEqual(Level.WARN)) {
            return;
        }

        slackWebhookService.send(new SlackMessageVo(eventObject.getFormattedMessage()));
    }
}