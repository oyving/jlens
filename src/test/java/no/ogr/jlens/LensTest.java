package no.ogr.jlens;

import org.junit.Test;
import static org.fest.assertions.Assertions.*;

public class LensTest {
    @Test
    public void check_get_set() {
        Pair<Integer, Character> pair = new Pair<>(1, 'a');
        Lens<Pair<Integer, Character>, Integer> first = first();

        assertThat(first.get(pair)).isEqualTo(1);
        assertThat(first.set(pair, 2)).isEqualTo(new Pair<>(2, 'a'));
    }

    @Test
    public void check_lens_laws() {
        Pair<Integer, Character> pair = new Pair<>(1, 'a');
        Lens<Pair<Integer, Character>, Integer> first = first();

        // l.get(l.set(a, b)) == b
        assertThat(first.get(first.set(pair, 5))).isEqualTo(5);

        // l.set(l.set(a, b), c) == l.set(a, c)
        assertThat(first.set(first.set(pair, 2), 3)).isEqualTo(first.set(pair, 3));

        // l.set(l.get(a), a) == a
        assertThat(first.set(pair, first.get(pair))).isEqualTo(pair);
    }

    @Test
    public void check_composition() {
        Pair<Integer, Pair<Integer, Integer>> threes = new Pair<>(1, new Pair<>(2, 3));
        Lens<Pair<Integer, Pair<Integer, Integer>>, Pair<Integer, Integer>> second1 = second();
        Lens<Pair<Integer, Integer>, Integer> second2 = second();

        assertThat(second1.andThen(second2).get(threes)).isEqualTo(3);
        assertThat(second1.andThen(second2).set(threes, 4)).isEqualTo(new Pair<>(1, new Pair<>(2, 4)));
    }

    @Test
    public void check_modify() {
        Pair<Integer, Character> pair = new Pair<>(1, 'a');
        Lens<Pair<Integer, Character>, Integer> first = first();

        assertThat(first.modify(pair, a -> a + 1)).isEqualTo(first.set(pair, first.get(pair) + 1));
    }

    public static <A, B> Lens<Pair<A, B>, A> first() {
        return new Lens<>(a -> a._1, (a, b) -> a.withSecond(b));
    }

    public static <A, B> Lens<Pair<A, B>, B> second() {
        return new Lens<>(a -> a._2, (a, b) -> a.withFirst(b));
    }

    private static class Pair<A, B> {
        public final A _1;
        public final B _2;

        private Pair(A a, B b) {
            _1 = a;
            _2 = b;
        }

        public <T> Pair<A, T> withFirst(T x) {
            return new Pair<>(this._1, x);
        }

        public <T> Pair<T, B> withSecond(T x) {
            return new Pair<>(x, this._2);
        }

        @Override
        public String toString() {
            return "Pair{" + _1 + ", " + _2 + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (!_1.equals(pair._1)) return false;
            if (!_2.equals(pair._2)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = _1.hashCode();
            result = 31 * result + _2.hashCode();
            return result;
        }
    }
}
