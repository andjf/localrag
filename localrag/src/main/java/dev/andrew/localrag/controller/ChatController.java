package dev.andrew.localrag.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.andrew.localrag.properties.OpenAiProperties;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/chat")
public class ChatController extends AiController {

    public ChatController(OpenAiProperties openAiConfiguration) {
        super(openAiConfiguration);
    }

    @PostMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestBody String prompt) throws IOException {
        log.info("Initiated /v1/chat request with prmopt [{}]", prompt);

        ChatRequest chat = baseBuilder()
                .message(SystemMessage.of("You are an expert in AI designd to answer any question."))
                .message(UserMessage.of(prompt))
                .build();

        return super.chat(chat);
    }

}
