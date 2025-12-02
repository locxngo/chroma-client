package io.github.locxngo.chroma.embedding.google.gemini;

import io.github.locxngo.chroma.embedding.EmbeddingModelRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GeminiEmbeddingRequest extends EmbeddingModelRequest {
    private String model;
    private Content content;

    @Data
    @Accessors(chain = true)
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @Accessors(chain = true)
    public static class Part {
        private String text;
    }
}
