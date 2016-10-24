package org.derp.jaxl;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractTask<T> implements Task<T> {
    public <V> MapTask<T, V> map(Function<T, V> transformer) {
        return new MapTask<T, V>(this, transformer);
    }

    public <V, U> MergeTask<T, V, U> merge(Task<V> other, BiFunction<T, V, U> transformer) {
        return new MergeTask<T, V, U>(this, other, transformer);
    }

    public <V> FlatMapTask<T, V> flatMap(Function<T, Task<V>> transformer) {
        return new FlatMapTask<T, V>(this, transformer);
    }
}
