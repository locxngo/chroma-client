package io.github.locxngo.chroma.params;

import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.types.Where;
import io.github.locxngo.chroma.types.WhereDocument;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetCollectionRecordParam extends Parameter {
    private List<String> ids;
    private List<String> include;
    private Where where;
    private WhereDocument whereDocument;
    private int limit;
    private int offset;

    @Override
    public ErrorResponse validate() {
        if (limit <= 0) {
            limit = 0;
        }
        if (offset <= 0) {
            offset = 0;
        }
        return null;
    }
}
