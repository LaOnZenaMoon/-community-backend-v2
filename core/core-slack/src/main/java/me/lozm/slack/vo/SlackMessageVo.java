package me.lozm.slack.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.lozm.slack.code.AccessoryType;
import me.lozm.slack.code.BlockType;
import me.lozm.slack.code.ElementType;
import me.lozm.slack.code.TextType;

import java.util.List;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SlackMessageVo {

    private final String channel;
    private final String text;
    private final List<Block> blocks;
    private final List<Attachment> attachments;


    @Builder
    public SlackMessageVo(String text, String channel, List<Block> blocks, List<Attachment> attachments) {
        this.text = text;
        this.channel = channel;
        this.blocks = blocks;
        this.attachments = attachments;
    }

    public static SlackMessageVo from(String text) {
        return SlackMessageVo.builder()
                .text(text)
                .build();
    }

    public static SlackMessageVo of(String channel, String text) {
        return SlackMessageVo.builder()
                .channel(channel)
                .text(text)
                .build();
    }

    public static SlackMessageVo of(String channel, String text, List<Block> blocks) {
        return SlackMessageVo.builder()
                .channel(channel)
                .text(text)
                .blocks(blocks)
                .build();
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Block {
        private final String type;
        private final Text text;
        private final Accessory accessory;
        private final List<Element> elements;


        public static Block createDividerBlock() {
            return new Block(BlockType.DIVIDER.getCode(), null, null, null);
        }

        public static Block createElementBlock(BlockType type, List<Element> elements) {
            return new Block(type.getCode(), null, null, elements);
        }

        public static Block createTextBlock(BlockType type, Text text) {
            return new Block(type.getCode(), text, null, null);
        }

        public static Block createAccessoryBlock(BlockType type, Text text, Accessory accessory) {
            return new Block(type.getCode(), text, accessory, null);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Accessory {
        private final String type;
        private final String image_url;
        private final String alt_text;
        private final Text text;
        private final String value;

        public static Accessory createImageAccessory(String imageUrl, String altText) {
            return new Accessory(AccessoryType.IMAGE.getCode(), imageUrl, altText, null, null);
        }

        public static Accessory createButtonAccessory(Text text, String value) {
            return new Accessory(AccessoryType.BUTTON.getCode(), null, null, text, value);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Text {

        private final String type;
        private final String text;
        private final Boolean emoji;
        public static Text of(TextType textType, String text) {
            return new Text(textType.getCode(), text, null);
        }

        public static Text of(TextType textType, String text, Boolean isEmoji) {
            return new Text(textType.getCode(), text, isEmoji);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Element {
        private final String type;
        private final String image_url;
        private final String alt_text;
        private final String text;

        public static Element of(ElementType type, String text) {
            return new Element(type.getCode(), null, null, text);
        }

        public static Element of(ElementType type, String imageUrl, String altText) {
            return new Element(type.getCode(), imageUrl, altText, null);
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Attachment {

    }

}
