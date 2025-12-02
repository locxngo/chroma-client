package io.github.locxngo.chroma.embedding.ollama;

import lombok.Data;

import java.util.List;

@Data
public class OllamaEmbeddingResponse {
    private String model;
    private List<List<Float>> embeddings;
}
