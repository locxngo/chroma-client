package io.github.locxngo.chroma.types;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Space {
    COSINE("cosine"),
    L2("l2"),
    IP("ip");
    @JsonValue
    private final String value;

    Space(String value) {
        this.value = value;
    }
}
