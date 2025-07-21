package io.github.locxngo.chroma.types;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum QueryAction {
    ADD("add"),
    COUNT("count"),
    FORK("fork"),
    DELETE("delete"),
    GET("get"),
    QUERY("query"),
    UPDATE("update"),
    UPSERT("upsert");
    @JsonValue
    private final String value;

    QueryAction(String value) {
        this.value = value;
    }
}
