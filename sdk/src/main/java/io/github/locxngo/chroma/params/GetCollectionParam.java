package io.github.locxngo.chroma.params;

import io.github.locxngo.chroma.Constants;
import io.github.locxngo.chroma.HttpStatus;
import io.github.locxngo.chroma.embedding.EmbeddingFunction;
import io.github.locxngo.chroma.schemas.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Builder
@Getter
public class GetCollectionParam extends Parameter {
    private String nameOrId;
    private EmbeddingFunction embeddingFunction;

    @Override
    public ErrorResponse validate() {
        if (StringUtils.isAnyBlank(nameOrId)) {
            return new ErrorResponse().setErrorCode(HttpStatus.SC_BAD_REQUEST)
                .setError(Constants.ERROR_PARAMETER)
                .setMessage("Collection name or Id must not blank");
        }
        return null;
    }
}
