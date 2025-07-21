package io.github.locxngo.chroma.embedding;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class EmbeddingOption {
    private String baseUrl;
    private String apiKey;
    private String model;
    @Builder.Default
    private Map<String, Object> options = new HashMap<>();

    public static EmbeddingOption empty() {
        return EmbeddingOption.builder().build();
    }
}
