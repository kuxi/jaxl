package org.derp.jaxl;

import java.util.function.BiFunction;

public interface Task2<T, V> {
    <U> Task<U> map(BiFunction<T, V, U> transformer);

    // <U> Task3<T, V, U> join(Task<U> other);

    <U> Task<U> flatMap(BiFunction<T, V, Task<U>> transformer);
}
