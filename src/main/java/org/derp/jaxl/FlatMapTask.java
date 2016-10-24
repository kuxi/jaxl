package org.derp.jaxl;

import java.util.function.Function;

public final class FlatMapTask<T, V> extends AbstractTask<V> {
    private final Task<T> previous;
    private final Function<T, Task<V>> mapping;

    FlatMapTask(Task<T> previous, Function<T, Task<V>> mapping) {
        this.previous = previous;
        this.mapping = mapping;
    }
}

