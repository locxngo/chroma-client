package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.exception.CreateEmbeddingException;
import io.github.locxngo.chroma.schemas.EmbeddingFunctionConfig;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import okhttp3.Response;

import java.util.List;

public class DefaultEmbeddingFunction extends BasicEmbeddingFunction {
    private final EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

    public DefaultEmbeddingFunction(EmbeddingOption option) {
        super(option);
    }

    @Override
    protected String defaultBaseUrl() {
        return "dev.langchain4j";
    }

    @Override
    protected String defaultModel() {
        return "all-MiniLM-L6-v2";
    }

    @Override
    public EmbeddingModelRequest buildEmbeddingRequest(List<String> queryTexts) {
        return null;
    }

    @Override
    public List<io.github.locxngo.chroma.types.Embedding> buildListEmbeddings(Response response) {
        return List.of();
    }

    @Override
    public String name() {
        return "default";
    }

    @Override
    protected boolean isLocal() {
        return true;
    }

    @Override
    public List<io.github.locxngo.chroma.types.Embedding> embedText(List<String> queryTexts)
        throws CreateEmbeddingException {
        return queryTexts.stream()
            .map(s -> {
                TextSegment segment = TextSegment.from(s);
                Embedding embedding = embeddingModel.embed(segment).content();
                return io.github.locxngo.chroma.types.Embedding.from(embedding.vectorAsList());
            }).toList();
    }

    @Override
    public EmbeddingFunctionConfig toConfig() {
        return null;
    }
}
