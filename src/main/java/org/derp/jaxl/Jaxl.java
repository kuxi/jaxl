package org.derp.jaxl;

import java.util.concurrent.Future;

public final class Jaxl {
    public static <T> Task<T> request(Request<T> request) {
        return new RequestTask<T>(request);
    }

    public static <T> Future<T> run(Task<T> task) {
    }
}
