package com.epam.aix.estateassistant.service.dto;

import org.springframework.ai.chat.messages.MessageType;

public record MessageDto(MessageType messageType, String content) {
}
