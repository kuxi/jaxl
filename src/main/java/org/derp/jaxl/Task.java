package org.derp.jaxl;

import java.util.function.Function;

public interface Task<T> {
    <V> Task<V> map(Function<T, V> transformer);

    <V> Task2<T, V> join(Task<V> other);

    <V> Task<V> flatMap(Function<T, Task<V>> transformer);
}
