package org.derp.jaxl;

import java.util.function.Function;


public final class MapTask<T, V> extends AbstractTask<V> {
    private final Task<T> previous;
    private final Function<T, V> mapper;

    MapTask(Task<T> previous, Function<T, V> mapper) {
        this.previous = previous;
        this.mapper = mapper;
    }
}
