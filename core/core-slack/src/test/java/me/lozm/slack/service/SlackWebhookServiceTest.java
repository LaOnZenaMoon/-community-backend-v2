package me.lozm.slack.service;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import me.lozm.slack.code.BlockType;
import me.lozm.slack.code.ElementType;
import me.lozm.slack.code.TextType;
import me.lozm.slack.vo.SlackMessageVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ActiveProfiles("local")
@SpringBootTest
class SlackWebhookServiceTest {

    @Autowired
    private SlackWebhookService slackWebhookService;


    @DisplayName("기본 메세지 전송 성공")
    @Test
    void sendBaseMessage_success() {
        // Given
        final String message = "Hello world!";

        // When
        Response response = slackWebhookService.send(SlackMessageVo.from(message))
                .orElseThrow(IllegalArgumentException::new);

        // Then
        final HttpStatus.Series httpStatusSeries = HttpStatus.Series.valueOf(response.status());
        Assertions.assertEquals(HttpStatus.Series.SUCCESSFUL, httpStatusSeries);
    }

    @DisplayName("블록 메세지 전송 성공")
    @Test
    void sendBlockMessage_success() {
        // Given
        List<SlackMessageVo.Block> blockList = new ArrayList<>();
        blockList.add(SlackMessageVo.Block.createTextBlock(
                BlockType.SECTION,
                SlackMessageVo.Text.of(TextType.PLAIN_TEXT, "Looks like you have a scheduling conflict with this event:")
        ));
        blockList.add(SlackMessageVo.Block.createDividerBlock());
        blockList.add(SlackMessageVo.Block.createAccessoryBlock(
                BlockType.SECTION,
                SlackMessageVo.Text.of(TextType.MARK_DOWN, "*<fakeLink.toUserProfiles.com|Iris / Zelda 1-1>*\nTuesday, January 21 4:00-4:30pm\nBuilding 2 - Havarti Cheese (3)\n2 guests"),
                SlackMessageVo.Accessory.createImageAccessory("https://api.slack.com/img/blocks/bkb_template_images/notifications.png", "calendar thumbnail")
        ));
        blockList.add(SlackMessageVo.Block.createElementBlock(
                BlockType.CONTEXT,
                List.of(
                        SlackMessageVo.Element.of(
                                ElementType.IMAGE,
                                "https://api.slack.com/img/blocks/bkb_template_images/notificationsWarningIcon.png",
                                "notifications warning icon"
                        ),
                        SlackMessageVo.Element.of(
                                ElementType.MARK_DOWN,
                                "*Conflicts with Team Huddle: 4:15-4:30pm*"
                        )
                )
        ));
        blockList.add(SlackMessageVo.Block.createDividerBlock());
        blockList.add(SlackMessageVo.Block.createTextBlock(
                BlockType.SECTION,
                SlackMessageVo.Text.of(TextType.MARK_DOWN, "*Propose a new time:*")
        ));
        blockList.add(SlackMessageVo.Block.createAccessoryBlock(
                BlockType.SECTION,
                SlackMessageVo.Text.of(TextType.MARK_DOWN, "*Today - 4:30-5pm*\nEveryone is available: @iris, @zelda"),
                SlackMessageVo.Accessory.createButtonAccessory(
                        SlackMessageVo.Text.of(TextType.PLAIN_TEXT, "Choose", true),
                        "click_me_123"
                )
        ));
        blockList.add(SlackMessageVo.Block.createAccessoryBlock(
                BlockType.SECTION,
                SlackMessageVo.Text.of(TextType.MARK_DOWN, "*Tomorrow - 4-4:30pm*\nEveryone is available: @iris, @zelda"),
                SlackMessageVo.Accessory.createButtonAccessory(
                        SlackMessageVo.Text.of(TextType.PLAIN_TEXT, "Choose", true),
                        "click_me_123"
                )
        ));
        blockList.add(SlackMessageVo.Block.createAccessoryBlock(
                BlockType.SECTION,
                SlackMessageVo.Text.of(TextType.MARK_DOWN, "*Tomorrow - 6-6:30pm*\nSome people aren't available: @iris, ~@zelda~"),
                SlackMessageVo.Accessory.createButtonAccessory(
                        SlackMessageVo.Text.of(TextType.PLAIN_TEXT, "Choose", true),
                        "click_me_123"
                )
        ));
        blockList.add(SlackMessageVo.Block.createTextBlock(
                BlockType.SECTION,
                SlackMessageVo.Text.of(TextType.MARK_DOWN, "*<fakelink.ToMoreTimes.com|Show more times>*")
        ));

        // When
        Response response = slackWebhookService.send(SlackMessageVo.of(null, null, blockList))
                .orElseThrow(IllegalArgumentException::new);

        // Then
        final HttpStatus.Series httpStatusSeries = HttpStatus.Series.valueOf(response.status());
        Assertions.assertEquals(HttpStatus.Series.SUCCESSFUL, httpStatusSeries);
    }

}