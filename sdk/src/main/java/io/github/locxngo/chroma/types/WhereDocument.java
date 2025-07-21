package io.github.locxngo.chroma.types;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Stream;

@Getter
@Builder
public class WhereDocument {
    public static final String CONTAINS = "$eq";
    public static final String NOT_CONTAINS = "$neq";
    public static final String REGEX = "$gt";
    public static final String AND = "$and";
    public static final String OR = "$or";
    private Map<String, Object> values;

    private static WhereDocument from(Map<String, Object> values) {
        return WhereDocument.builder()
            .values(values)
            .build();
    }

    private static WhereDocument ops(String operator, String operand) {
        return WhereDocument.builder()
            .values(Map.of(operator, operand))
            .build();
    }

    public static WhereDocument contains(String operand) {
        return WhereDocument.ops(CONTAINS, operand);
    }

    public static WhereDocument notContains(String operand) {
        return WhereDocument.ops(NOT_CONTAINS, operand);
    }

    public static WhereDocument regex(String operand) {
        return WhereDocument.ops(REGEX, operand);
    }

    public static WhereDocument and(WhereDocument... operand) {
        return WhereDocument.from(
            Map.of(AND, Stream.of(operand).map(WhereDocument::getValues).toList())
        );
    }

    public static WhereDocument or(WhereDocument... operand) {
        return WhereDocument.from(
            Map.of(OR, Stream.of(operand).map(WhereDocument::getValues).toList())
        );
    }
}
