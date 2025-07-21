package io.github.locxngo.chroma.types;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Getter
@Builder
public class Where {
    public static final String EQUAL = "$eq";
    public static final String NOT_EQUAL = "$neq";
    public static final String GREATER_THAN = "$gt";
    public static final String GREATER_THAN_OR_EQUAL = "$gte";
    public static final String LESS_THAN = "$lt";
    public static final String LESS_THAN_OR_EQUAL = "$lte";
    public static final String IN = "$in";
    public static final String NOT_IN = "$nin";
    public static final String AND = "$and";
    public static final String OR = "$or";
    private Map<String, Object> values;

    private static Where from(Map<String, Object> values) {
        return Where.builder()
            .values(values)
            .build();
    }

    private static <I> Where ops(String metaField, String operator, I operand) {
        return Where.builder()
            .values(Map.of(metaField, Map.of(operator, operand)))
            .build();
    }

    public static <I> Where eq(String metaField, I operand) {
        return Where.from(Map.of(metaField, operand));
    }

    public static <I> Where neq(String metaField, I operand) {
        return Where.ops(metaField, NOT_EQUAL, operand);
    }


    public static <I> Where gt(String metaField, I operand) {
        return Where.ops(metaField, GREATER_THAN, operand);
    }

    public static <I> Where gte(String metaField, I operand) {
        return Where.ops(metaField, GREATER_THAN_OR_EQUAL, operand);
    }

    public static <I> Where lt(String metaField, I operand) {
        return Where.ops(metaField, LESS_THAN, operand);
    }

    public static <I> Where lte(String metaField, I operand) {
        return Where.ops(metaField, LESS_THAN_OR_EQUAL, operand);
    }

    public static <I> Where in(String metaField, I... operand) {
        return Where.ops(metaField, IN, List.of(operand));
    }

    public static <I> Where nin(String metaField, I operand) {
        return Where.ops(metaField, NOT_IN, List.of(operand));
    }

    public static Where and(Where... operand) {
        return Where.from(
            Map.of(AND, Stream.of(operand).map(Where::getValues).toList())
        );
    }

    public static Where or(Where... operand) {
        return Where.from(
            Map.of(OR, Stream.of(operand).map(Where::getValues).toList())
        );
    }
}
