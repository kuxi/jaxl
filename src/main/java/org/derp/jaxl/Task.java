package org.derp.jaxl;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Task<T> {
    <V> Task<V> map(Function<T, V> transformer);

    <V, U> Task<U> merge(Task<V> other, BiFunction<T, V, U> transformer);

    <V> Task<V> flatMap(Function<T, Task<V>> transformer);
}
