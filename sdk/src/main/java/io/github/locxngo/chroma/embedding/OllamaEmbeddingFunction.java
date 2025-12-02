package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.embedding.ollama.OllamaEmbeddingRequest;
import io.github.locxngo.chroma.embedding.ollama.OllamaEmbeddingResponse;
import io.github.locxngo.chroma.exception.CreateEmbeddingException;
import io.github.locxngo.chroma.types.Embedding;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OllamaEmbeddingFunction extends BasicEmbeddingFunction {

    public static OllamaEmbeddingFunction fromConfig(Map<String, Object> config) {
        return new OllamaEmbeddingFunction(
                EmbeddingOption.builder()
                        .baseUrl((String) config.getOrDefault("baseUrl", null))
                        .model((String) config.getOrDefault("model", null))
                        .options(config)
                        .build());
    }

    public OllamaEmbeddingFunction(EmbeddingOption option) {
        super(option);
    }

    @Override
    protected String defaultBaseUrl() {
        return "http://localhost:11434/api/embed";
    }

    @Override
    protected String defaultModel() {
        return "nomic-embed-text";
    }

    @Override
    public EmbeddingModelRequest buildEmbeddingRequest(List<String> queryTexts) {
        return new OllamaEmbeddingRequest()
                .setModel(getModel())
                .setInput(queryTexts);
    }

    @Override
    public List<Embedding> buildListEmbeddings(Response response) {
        try {
            if (response.body() == null) {
                throw new CreateEmbeddingException("Response body is null");
            }
            OllamaEmbeddingResponse result = getObjectMapper().readValue(
                    response.body().string(), OllamaEmbeddingResponse.class);
            List<Embedding> embeddings = new ArrayList<>();
            if (result.getEmbeddings() != null) {
                for (List<Float> embedding : result.getEmbeddings()) {
                    embeddings.add(Embedding.from(embedding));
                }
            }
            return embeddings;
        } catch (IOException e) {
            throw new CreateEmbeddingException("Failed to deserialize response", e);
        }
    }

    @Override
    public String name() {
        return AIPlatform.OLLAMA.getValue();
    }
}
