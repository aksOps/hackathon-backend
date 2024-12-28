package io.github.random.code.space.video.streaming.Service;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Service
@Log4j2
public class VideoCategorizer {


    @Autowired
    private EmbeddingModel embeddingModel;
    InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
    Map<String,String> store=new HashMap<>();

    public void getCategories(String videoDescription) {
        videoDescription=videoDescription.replace(".mp4","");
        embeddingModel.embed(videoDescription).content();
        String x=embeddingStore.add(embeddingModel.embed(videoDescription).content());
        store.put(x,videoDescription);
    }

    public Set<String> compare(String videoDescription) {
        videoDescription=videoDescription.replace(".mp4","");
        Set<String> suggestions=new HashSet<>();
        EmbeddingSearchRequest embeddingSearchRequest=new EmbeddingSearchRequest(embeddingModel.embed(videoDescription).content(),10,0.9,null);
        embeddingStore.search(embeddingSearchRequest).matches().forEach(textSegmentEmbeddingMatch -> {
            var embedded=textSegmentEmbeddingMatch.embeddingId();
            var videoName=store.get(embedded);
            suggestions.add(videoName+".mp4");

        });
        return suggestions;

    }
}