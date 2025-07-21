package io.github.locxngo.chroma.embedding.cohere;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Truncate {
    NONE("NONE"),
    START("START"),
    END("END");
    @JsonValue
    private final String value;

    Truncate(String value) {
        this.value = value;
    }
}
