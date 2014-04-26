package no.ogr.jlens.collections;

import no.ogr.jlens.Lens;

import java.util.*;

public class CollectionsLens {
    public static <A> Lens<Set<A>, Boolean> set(A key) {
        return new Lens<>(
                (a) -> a.contains(key),
                (a, b) -> {
                    Set<A> c = new HashSet<>(a);
                    if (b) {
                        c.add(key);
                    } else {
                        c.remove(key);
                    }
                    return c;
                }
        );
    }

    public static <A> Lens<List<A>, A> list(int index) {
        return new Lens<>(
                (a) -> a.get(index),
                (a, b) -> {
                    List<A> c = new ArrayList<>(a);
                    c.set(index, b);
                    return c;
                }
        );
    }

    public static <A, B> Lens<Map<A, B>, B> map(A key) {
        return new Lens<>(
                (a) -> a.get(key),
                (a, b) -> {
                    Map<A, B> c = new LinkedHashMap<>(a);
                    c.put(key, b);
                    return c;
                }
        );
    }
}
