package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.exception.UnknownEmbeddingFunctionException;

import java.util.Map;

public class EmbeddingFunctionFactory {

    private EmbeddingFunctionFactory() {
    }

    /**
     * Creates and returns an instance of {@link EmbeddingFunction} from given name and map of config
     */
    public static EmbeddingFunction get(String name, Map<String, Object> config) {
        AIPlatform model = AIPlatform.fromValue(name);
        return switch (model) {
            case OPENAI -> new OpenAIEmbeddingFunction(EmbeddingOption.from(AIPlatform.OPENAI, config));
            case VOYAGE_AI -> new VoyageAIEmbeddingFunction(EmbeddingOption.from(AIPlatform.VOYAGE_AI, config));
            case COHERE -> new CohereEmbeddingFunction(EmbeddingOption.from(AIPlatform.COHERE, config));
            case MISTRAL -> new MistralAIEmbeddingFunction(EmbeddingOption.from(AIPlatform.MISTRAL, config));
            default -> throw new UnknownEmbeddingFunctionException(name);
        };
    }

    /**
     * Creates and returns an instance of {@link EmbeddingFunction} from given name and config
     */
    public static EmbeddingFunction get(String name, EmbeddingOption config) {
        AIPlatform model = AIPlatform.fromValue(name);
        return switch (model) {
            case OPENAI -> new OpenAIEmbeddingFunction(config);
            case VOYAGE_AI -> new VoyageAIEmbeddingFunction(config);
            case COHERE -> new CohereEmbeddingFunction(config);
            case MISTRAL -> new MistralAIEmbeddingFunction(config);
            default -> throw new UnknownEmbeddingFunctionException(name);
        };
    }
}
