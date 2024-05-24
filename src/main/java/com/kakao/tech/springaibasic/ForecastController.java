package com.kakao.tech.springaibasic;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastController {

    private final ChatClient chatClient;

    @Value("classpath:/forecast.png")
    Resource forecastImageResource;

    public ForecastController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/forecast")
    public String forecast(@RequestParam String ask) throws Exception {
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(ask)
                        .media(MimeTypeUtils.IMAGE_JPEG, forecastImageResource))
                .call()
                .content();
    }
}
