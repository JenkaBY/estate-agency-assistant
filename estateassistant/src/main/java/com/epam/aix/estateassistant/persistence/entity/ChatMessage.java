package com.epam.aix.estateassistant.persistence.entity;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class ChatMessage implements Comparable<ChatMessage> {
    private String messageId;
    private String chatId;
    private String sender;
    private String content;
    private ZonedDateTime publishedAt;

    @Override
    public int compareTo(ChatMessage o) {
        if (this.publishedAt == null && o.publishedAt == null) return 0;
        if (this.publishedAt == null) return -1;
        if (o.publishedAt == null) return 1;
        return this.publishedAt.compareTo(o.publishedAt);
    }
}
