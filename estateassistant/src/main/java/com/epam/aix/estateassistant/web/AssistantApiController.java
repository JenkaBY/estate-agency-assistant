package com.epam.aix.estateassistant.web;

import com.epam.aix.estateassistant.persistence.entity.Chat;
import com.epam.aix.estateassistant.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
@RestController
public class AssistantApiController {

    private final ChatService chatService;

    @GetMapping("/{chatId}")
    public Chat getChat(@RequestParam("chatId") String chatId) {

        return chatService.findChatById(chatId);
    }

    @PutMapping("/{chatId}")
    public String talk(@RequestParam("chatId") String chatId, @RequestBody UserMessageRequest request) {
        log.info("talk {} for user input: {}", chatId, request);
        return chatService.talk(chatId, request.message());
    }
}
