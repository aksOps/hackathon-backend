package io.github.random.code.space.video.streaming.config;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@Log4j2
public class WebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        var path = new URL(req.getRequestURL().toString()).getPath();
        if (path.contains("login") || path.contains("signup") ||  path.contains("video/")) {
            chain.doFilter(request, response);
        } else {
            if (req.getHeader("Authentication-Info") == null || req.getHeader("Authentication-Info").isEmpty()) {
                ((HttpServletResponse) response).sendError(401);
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("nomic-embed-text").build();
    }

}
