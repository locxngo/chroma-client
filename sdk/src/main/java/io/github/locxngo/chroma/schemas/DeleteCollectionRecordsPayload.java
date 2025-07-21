package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
public class DeleteCollectionRecordsPayload extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> where;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> whereDocument;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> ids;
}
