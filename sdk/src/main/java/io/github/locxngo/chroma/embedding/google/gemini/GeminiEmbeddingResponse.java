package io.github.locxngo.chroma.embedding.google.gemini;

import lombok.Data;

import java.util.List;

@Data
public class GeminiEmbeddingResponse {
    private EmbeddingData embedding;

    @Data
    public static class EmbeddingData {
        private List<Float> values;
    }
}
