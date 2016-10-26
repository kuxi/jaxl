package org.derp.jaxl;

import java.util.function.Function;

public abstract class AbstractTask<T> implements Task<T> {
    public <V> MapTask<T, V> map(Function<T, V> transformer) {
        return new MapTask<T, V>(this, transformer);
    }

    public <V> JoinTask<T, V> join(Task<V> other) {
        return new JoinTask<T, V>(this, other);
    }

    public <V> FlatMapTask<T, V> flatMap(Function<T, Task<V>> transformer) {
        return new FlatMapTask<T, V>(this, transformer);
    }
}
