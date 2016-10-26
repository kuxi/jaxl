package org.derp.jaxl;

import java.util.function.Function;


public final class MapTask<T, V> extends AbstractTask<V> {
    final Task<T> parent;
    private final Function<T, V> mapper;

    MapTask(Task<T> previous, Function<T, V> mapper) {
        this.parent = previous;
        this.mapper = mapper;
    }
}
