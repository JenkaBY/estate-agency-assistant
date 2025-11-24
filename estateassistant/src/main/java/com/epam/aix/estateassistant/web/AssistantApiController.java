package com.epam.aix.estateassistant.web;

import com.epam.aix.estateassistant.persistence.entity.Chat;
import com.epam.aix.estateassistant.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
@RestController
public class AssistantApiController {

    private final ChatService chatService;

    @GetMapping("/{chatId}")
    public Chat getChat(@CookieValue("chatId") String sessionId) {
        String chatId = sessionId;
        return chatService.findChatById(chatId);
    }

    @PutMapping("/{chatId}")
    public String updateChat(@CookieValue("chatId") String sessionId) {
        String chatId = sessionId;
        // You can use chatId for further logic
        return "Estate Assistant is running! ChatId: " + chatId;
    }
}
