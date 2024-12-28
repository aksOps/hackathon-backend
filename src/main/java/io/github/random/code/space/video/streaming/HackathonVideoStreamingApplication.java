package io.github.random.code.space.video.streaming;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static dev.langchain4j.model.chat.request.ResponseFormat.TEXT;

@SpringBootApplication
@Log4j2
public class HackathonVideoStreamingApplication {
    public static void main(String[] args) {
        SpringApplication.run(HackathonVideoStreamingApplication.class, args);
    }

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OllamaChatModel.builder().baseUrl("http://localhost:11434").modelName("llama3.2:1b").responseFormat(TEXT).build();
    }
}
