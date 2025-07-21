package io.github.locxngo.chroma.params;

import io.github.locxngo.chroma.Constants;
import io.github.locxngo.chroma.HttpStatus;
import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.types.Metadata;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Builder
@Getter
public class UpdateCollectionParam extends Parameter {
    private String newName;
    private String collectionId;
    private Metadata metadata;

    @Override
    public ErrorResponse validate() {
        if (StringUtils.isAnyBlank(newName, collectionId)) {
            return new ErrorResponse().setErrorCode(HttpStatus.SC_BAD_REQUEST)
                .setError(Constants.ERROR_PARAMETER)
                .setMessage("Collection Id and new name must not blank");
        }
        return null;
    }
}
