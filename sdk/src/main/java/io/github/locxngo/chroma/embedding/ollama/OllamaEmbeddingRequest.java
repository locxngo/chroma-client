package io.github.locxngo.chroma.embedding.ollama;

import io.github.locxngo.chroma.embedding.EmbeddingModelRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class OllamaEmbeddingRequest extends EmbeddingModelRequest {
    private String model;
    private List<String> input;
}
