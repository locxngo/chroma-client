package io.github.locxngo.chroma.embedding.voyageai;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum InputType {
    QUERY("query"),
    DOCUMENT("document");
    @JsonValue
    private final String value;

    InputType(String value) {
        this.value = value;
    }
}
