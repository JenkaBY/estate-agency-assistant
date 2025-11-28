package com.epam.aix.estateassistant.web;

import com.epam.aix.estateassistant.service.ChatService;
import com.epam.aix.estateassistant.service.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class AssistantApiController {

    private final ChatService chatService;

    @GetMapping("/{chatId}")
    public List<MessageDto> getChat(@PathVariable("chatId") String chatId) {
        return chatService.findChatById(chatId);
    }

    @PutMapping("/{chatId}")
    public String talk(@PathVariable("chatId") String chatId, @RequestBody UserMessageRequest request) {
        log.info("talk {} for user input: {}", chatId, request);
        return chatService.talk(chatId, request.message());
    }
}
