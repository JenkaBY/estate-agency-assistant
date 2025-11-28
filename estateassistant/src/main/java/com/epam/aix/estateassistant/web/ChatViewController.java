package com.epam.aix.estateassistant.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatViewController {

    @GetMapping("/")
    public String index() {
        return "redirect:/chat";
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}

