package io.github.locxngo.chroma.params;

import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.types.Where;
import io.github.locxngo.chroma.types.WhereDocument;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DeleteCollectionRecordParam extends Parameter {
    private List<String> ids;
    private Where where;
    private WhereDocument whereDocument;

    @Override
    public ErrorResponse validate() {
        return null;
    }
}
