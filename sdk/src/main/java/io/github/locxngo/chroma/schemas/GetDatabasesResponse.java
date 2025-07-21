package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class GetDatabasesResponse extends ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<GetDatabaseResponse> databases;
}
