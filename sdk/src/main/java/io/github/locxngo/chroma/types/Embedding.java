package io.github.locxngo.chroma.types;

import java.util.ArrayList;
import java.util.List;

public class Embedding extends ArrayList<Float> {

    public static Embedding from(List<Float> list) {
        Embedding e = new Embedding();
        e.addAll(list);
        return e;
    }
}
