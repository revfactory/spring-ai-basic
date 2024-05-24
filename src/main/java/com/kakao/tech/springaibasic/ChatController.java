package com.kakao.tech.springaibasic;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "robin") String name) {
        PromptTemplate promptTemplate = new PromptTemplate("Hello! My name is {name}");
        Prompt prompt = promptTemplate.create(Map.of("name", name));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();

    }

}
