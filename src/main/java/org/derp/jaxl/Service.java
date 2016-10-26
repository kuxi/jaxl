package org.derp.jaxl;

import java.util.concurrent.CompletableFuture;

public interface Service {
    <Req extends Request<Res>, Res> CompletableFuture<Res> getResult(Req request);
}
