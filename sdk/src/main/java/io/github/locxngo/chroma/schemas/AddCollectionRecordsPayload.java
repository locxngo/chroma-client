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
public class AddCollectionRecordsPayload extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> documents;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Embedding> embeddings;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> ids;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> uris;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Metadata> metadatas;
}
