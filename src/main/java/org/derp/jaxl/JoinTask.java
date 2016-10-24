package org.derp.jaxl;

import java.util.function.BiFunction;

public final class JoinTask<T, V> implements Task2<T, V> {
    private final Task<T> task1;
    private final Task<V> task2;

    JoinTask(Task<T> task1, Task<V> task2) {
        this.task1 = task1;
        this.task2 = task2;
    }

    public <U> Task<U> map(BiFunction<T, V, U> transformer) {
        return null;
    }

    public <U> Task<U> flatMap(BiFunction<T, V, Task<U>> transformer) {
        return null;
    }
}
