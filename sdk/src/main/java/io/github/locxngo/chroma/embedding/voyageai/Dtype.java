package io.github.locxngo.chroma.embedding.voyageai;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Dtype {
    FLOAT("float"),
    INT8("int8"),
    UINT8("uint8"),
    BINARY("binary"),
    U_BINARY("ubinary");
    @JsonValue
    private final String value;

    Dtype(String value) {
        this.value = value;
    }
}
