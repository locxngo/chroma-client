package io.github.locxngo.chroma.params;

import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.types.Embedding;
import io.github.locxngo.chroma.types.Image;
import io.github.locxngo.chroma.types.Metadata;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AddCollectionRecordParam extends Parameter {
    private List<String> documents;
    private List<Embedding> embeddings;
    private List<String> ids;
    private List<Metadata> metadatas;
    private List<String> uris;
    private List<Image> images;
    @Builder.Default
    private boolean multiModel = false;

    @Override
    public ErrorResponse validate() {
        return null;
    }
}
