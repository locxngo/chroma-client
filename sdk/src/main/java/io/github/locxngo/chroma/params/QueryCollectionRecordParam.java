package io.github.locxngo.chroma.params;

import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.types.Where;
import io.github.locxngo.chroma.types.WhereDocument;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QueryCollectionRecordParam extends Parameter {
    private List<String> queryTexts;
    @Builder.Default
    private int numberOfResults = 5;
    private List<String> ids;
    private List<String> include;
    private Where where;
    private WhereDocument whereDocument;

    @Override
    public ErrorResponse validate() {
        if (numberOfResults <= 0) {
            numberOfResults = 0;
        }
        return null;
    }
}
