package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.exception.UnknownEmbeddingFunctionException;

import java.util.Map;

public class EmbeddingFunctionFactory {

    private EmbeddingFunctionFactory() {
    }

    public static EmbeddingFunction get(String name, Map<String, Object> config) {
        return switch (name) {
            case "openai" -> OpenAIEmbeddingFunction.fromConfig(config);
            case "voyageai" -> VoyageAIEmbeddingFunction.fromConfig(config);
            default -> throw new UnknownEmbeddingFunctionException(name);
        };
    }
}
