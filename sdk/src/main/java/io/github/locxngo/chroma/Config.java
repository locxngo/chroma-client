package io.github.locxngo.chroma;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class Config {
    @Builder.Default
    private String endpoint = "http://localhost:8000";
    @Builder.Default
    private String tenant = "default_tenant";
    @Builder.Default
    private String database = "default_database";
    private Map<String, String> parameters;

    /**
     * Returns default configuration
     */
    public static Config defaultConfig() {
        return Config.builder().build();
    }
}
