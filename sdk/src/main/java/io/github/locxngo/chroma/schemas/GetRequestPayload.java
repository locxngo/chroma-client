package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
public class GetRequestPayload extends PrintableObject {
    @JsonProperty("where")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> where;
    @JsonProperty("where_document")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> whereDocument;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> ids;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> include;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int limit;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int offset;
}
