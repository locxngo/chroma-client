package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CreateDatabasePayload extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;
}
