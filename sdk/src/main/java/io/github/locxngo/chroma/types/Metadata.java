package io.github.locxngo.chroma.types;

import java.util.HashMap;
import java.util.Map;

public class Metadata extends HashMap<String, Object> {

    public static Metadata from(Map<String, Object> map) {
        Metadata m = new Metadata();
        m.putAll(map);
        return m;
    }
}
