package io.github.locxngo.chroma.schemas;

import io.github.locxngo.chroma.types.Embedding;
import io.github.locxngo.chroma.types.Metadata;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class QueryResponse extends ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<List<Float>> distances;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<List<String>> documents;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<List<Embedding>> embeddings;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<List<String>> uris;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<List<String>> ids;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> include;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<List<Metadata>> metadatas;
}
