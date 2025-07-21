package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class SpannConfiguration extends PrintableObject {
    @JsonProperty("ef_construction")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int efConstruction;
    @JsonProperty("ef_search")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int efSearch;
    @JsonProperty("max_neighbors")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int maxNeighbors;
    @JsonProperty("merge_threshold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int mergeThreshold;
    @JsonProperty("reassign_neighbor_count")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int reAssignNeighborCount;
    @JsonProperty("space")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String space;
    @JsonProperty("split_threshold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int splitThreshold;
    @JsonProperty("write_nprobe")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int writeNProbe;
}
