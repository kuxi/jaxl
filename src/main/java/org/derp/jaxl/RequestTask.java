package org.derp.jaxl;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.Optional;


public final class RequestTask<T> extends AbstractTask<T> {
    final Request<T> request;

    RequestTask(Request<T> request) {
        this.request = request;
    }
}

