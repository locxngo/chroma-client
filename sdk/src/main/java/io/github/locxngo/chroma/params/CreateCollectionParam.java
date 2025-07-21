package io.github.locxngo.chroma.params;

import io.github.locxngo.chroma.Constants;
import io.github.locxngo.chroma.HttpStatus;
import io.github.locxngo.chroma.embedding.EmbeddingFunction;
import io.github.locxngo.chroma.schemas.CollectionConfiguration;
import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.types.Metadata;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
public class CreateCollectionParam extends Parameter {
    private String name;
    private boolean getOrCreate;
    private EmbeddingFunction embeddingFunction;
    private CollectionConfiguration config;
    private Metadata metadata;

    @Override
    public ErrorResponse validate() {
        if (StringUtils.isAnyBlank(name)) {
            return new ErrorResponse().setErrorCode(HttpStatus.SC_BAD_REQUEST)
                .setError(Constants.ERROR_PARAMETER)
                .setMessage("Collection name must not blank");
        }
        return null;
    }
}
