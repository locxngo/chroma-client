package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CollectionConfiguration extends PrintableObject {
    @JsonProperty("embedding_function")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EmbeddingFunctionConfig embeddingFunction;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HnswConfiguration hnsw;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SpannConfiguration spann;
}
