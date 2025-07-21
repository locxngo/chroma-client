package io.github.locxngo.chroma.schemas;

import io.github.locxngo.chroma.types.Embedding;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
public class QueryRequestPayload extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> ids;
    @JsonProperty("n_results")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int numberOfResults;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> include;
    @JsonProperty("query_embeddings")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Embedding> queryEmbeddings;
    @JsonProperty("where")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> where;
    @JsonProperty("where_document")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> whereDocument;
}
