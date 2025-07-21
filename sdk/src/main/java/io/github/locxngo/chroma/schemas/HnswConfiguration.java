package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class HnswConfiguration extends PrintableObject {
    @JsonProperty("ef_construction")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int efConstruction;
    @JsonProperty("ef_search")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int efSearch;
    @JsonProperty("max_neighbors")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int maxNeighbors;
    @JsonProperty("resize_factor")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double resizeFactor;
    @JsonProperty("sync_threshold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int syncThreshold;
    @JsonProperty("space")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String space;
}
