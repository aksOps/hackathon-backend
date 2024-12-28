package io.github.random.code.space.video.streaming;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import io.github.random.code.space.video.streaming.Service.HistoryService;
import io.github.random.code.space.video.streaming.Service.VideoCategorizer;
import io.github.random.code.space.video.streaming.Service.VideoService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@Log4j2
public class HackathonVideoStreamingApplication implements CommandLineRunner{


    @Autowired
    VideoService videoService;

    @Autowired
    HistoryService historyService;

    @Autowired
    VideoCategorizer videoCategorizer;

    public static void main(String[] args) {
        SpringApplication.run(HackathonVideoStreamingApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        videoService.listAllVideos().forEach(videoCategorizer::getCategories);
        log.info("Embedding loaded");
    }
}
