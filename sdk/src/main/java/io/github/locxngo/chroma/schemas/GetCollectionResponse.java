package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
public class GetCollectionResponse extends ErrorResponse {
    @JsonProperty("configuration_json")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CollectionConfiguration configurationJson;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String database;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String id;
    @JsonProperty("log_position")
    private int logPosition;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String tenant;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int version;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int dimension;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> metadata;
}
