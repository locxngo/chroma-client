package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.embedding.mistral.TextEmbeddingModelRequest;
import io.github.locxngo.chroma.embedding.mistral.TextEmbeddingModelResponse;
import io.github.locxngo.chroma.exception.CreateEmbeddingException;
import io.github.locxngo.chroma.types.Embedding;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MistralAIEmbeddingFunction extends BasicEmbeddingFunction {
    public MistralAIEmbeddingFunction(EmbeddingOption option) {
        super(option);
    }

    public static MistralAIEmbeddingFunction fromConfig(Map<String, Object> config) {
        return new MistralAIEmbeddingFunction(
            EmbeddingOption.builder()
                .apiKey(System.getenv("MISTRAL_API_KEY"))
                .model((String) config.getOrDefault("model", null))
                .options(config)
                .build()
        );
    }

    @Override
    protected String defaultBaseUrl() {
        return "https://api.mistral.ai/v1/embeddings";
    }

    @Override
    protected String defaultModel() {
        return "mistral-embed";
    }

    @Override
    public EmbeddingModelRequest buildEmbeddingRequest(List<String> queryTexts) {
        return new TextEmbeddingModelRequest()
            .setInput(queryTexts)
            .setModel(getModel())
            .setOutputType(Dtype.FLOAT);
    }

    @Override
    public List<Embedding> buildListEmbeddings(Response response) {
        if (response.body() == null) {
            throw new CreateEmbeddingException("Response body from Mistral is blank");
        }
        try {
            TextEmbeddingModelResponse<Float> result = getObjectMapper().readValue(
                response.body().string(), TextEmbeddingModelResponse.OutputFloat.class
            );
            List<Embedding> results = new ArrayList<>();
            result.getData().forEach(o -> results.add(Embedding.from(o.getEmbedding())));
            return results;
        } catch (IOException exception) {
            throw new CreateEmbeddingException("Failed to deserialize response", exception);
        }
    }

    @Override
    public String name() {
        return AIPlatform.MISTRAL.getValue();
    }
}
