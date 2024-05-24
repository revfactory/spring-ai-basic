package com.kakao.tech.springaibasic;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @GetMapping("/assistant")
    public String assistant() {
ChatResponse response = chatClient.prompt()
        .system("Hello! How can I help you today?")
        .user("Who won the world series in 2020?")
        .messages(new AssistantMessage("The Los Angeles Dodgers won the World Series in 2020."))
        .user("Where was it played?")
        .options(OpenAiChatOptions.builder().withModel("gpt-3.5-turbo").build())
        .call().chatResponse();

return response.getResult().getOutput().getContent();
    }

}
