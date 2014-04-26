package no.ogr.jlens;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Lens<A, B> {
    private final Function<A, B> getter;
    private final BiFunction<A, B, A> setter;

    public Lens(Function<A, B> get, BiFunction<A, B, A> set) {
        this.getter = get;
        this.setter = set;
    }

    public B get(A a) {
        return getter.apply(a);
    }

    public A set(A a, B b) {
        return setter.apply(a, b);
    }

    public A modify(A a, Function<B, B> f) {
        return set(a, f.apply(get(a)));
    }

    public <C> Lens<C, B> compose(Lens<C, A> that) {
        return new Lens<>(
                (C c) -> get(that.get(c)),
                (C c, B b) -> that.modify(c, (a) -> set(a, b))
        );
    }

    public <C> Lens<A, C> andThen(Lens<B, C> that) {
        return that.compose(this);
    }

    private static <T> Lens<T, T> self() {
        return new Lens<T, T>((a) -> a, (x, a) -> a);
    }
}
