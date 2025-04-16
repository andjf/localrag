package dev.andrew.localrag.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.andrew.localrag.model.SentenceEmbedding;
import dev.andrew.localrag.service.AiService;
import dev.andrew.localrag.service.NlpService;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1")
public class AiController {

    AiService aiService;
    NlpService nlpService;

    public AiController(AiService aiService, NlpService nlpService) {
        this.aiService = aiService;
        this.nlpService = nlpService;
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestBody String prompt) throws IOException {
        log.info("Initiated /v1/chat request with prmopt [{}]", prompt);

        ChatRequest chat = this.aiService.chatRequestBuilder()
                .message(SystemMessage.of("You are an expert in AI designd to answer any question."))
                .message(UserMessage.of(prompt))
                .build();

        return this.aiService.chat(chat);
    }

    @PostMapping(value = "/embeddings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public List<SentenceEmbedding> embedding(@RequestBody String text) throws IOException {
        log.info("Initiated /v1/embeddings request with text [{}]", text);
        List<String> sentences = Arrays.asList(this.nlpService.sentences(text));
        return this.aiService.sentenceEmbeddings(sentences);
    }

}
