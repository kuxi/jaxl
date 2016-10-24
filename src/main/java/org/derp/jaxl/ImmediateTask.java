package org.derp.jaxl;

public final class ImmediateTask<T> extends AbstractTask<T> {
    private final T value;
    ImmediateTask(T value) {
        this.value = value;
    }
}
