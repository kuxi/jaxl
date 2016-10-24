package org.derp.jaxl;

import java.util.function.BiFunction;


public final class MergeTask<T, V, U> extends AbstractTask<U> {
    private final Task<T> task1;
    private final Task<V> task2;
    private final BiFunction<T, V, U> transformer;

    MergeTask(Task<T> task1, Task<V> task2, BiFunction<T, V, U> transformer) {
        this.task1 = task1;
        this.task2 = task2;
        this.transformer = transformer;
    }
}
