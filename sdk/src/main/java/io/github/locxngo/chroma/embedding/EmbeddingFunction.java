package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.exception.CreateEmbeddingException;
import io.github.locxngo.chroma.schemas.EmbeddingFunctionConfig;
import io.github.locxngo.chroma.types.Embedding;

import java.util.List;

public interface EmbeddingFunction {

    /**
     * return list embeddings of given list of texts
     *
     * @throws CreateEmbeddingException if it can not connect to embedding model or get exception while
     *                                  serialize/deserialize data
     */
    List<Embedding> embedText(List<String> queryTexts) throws CreateEmbeddingException;

    /**
     * returns name of embedding function
     */
    String name();

    /**
     * returns recommended space of embedding function
     */
    String defaultSpace();

    /**
     * returns a list of supported spaces
     */
    List<String> supportedSpaces();

    /**
     * returns an instance of {@link EmbeddingFunctionConfig}
     */
    EmbeddingFunctionConfig toConfig();
}
