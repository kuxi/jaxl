package org.derp.jaxl;

import com.google.common.reflect.TypeToken;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class Jaxl {
    Context context;

    public <Req extends Request<Res>, Res> void register(TypeToken<Req> requestType, Service service) {
    }

    public static <Res> Task<Res> request(Request<Res> request) {
        return new RequestTask<Res>(request);
    }

    public <Res> CompletableFuture<Res> run(Task<Res> task) {
        // GET (DAG, Cache)
        Dag dag = evaluate(task);
        Cache cache = new Cache();
        return execute(dag, cache);
    }

    private <Res> Dag evaluate(Task<Res> task) {
        return Dag.withTask(task);
    }

    private <Res> Dag evaluate(RequestTask<Res> task) {
        return Dag.withTask(task);
    }

    private <Res> Dag evaluate(ImmediateTask<Res> task) {
        return Dag.withTask(task);
    }

    private <Res> Dag evaluate(MapTask<Res, ?> task) {
        Dag dag = evaluate(task.parent);
        dag.depend(dag.lookup(task.parent), task);
        return dag;
    }

    private <Res> CompletableFuture<Res> execute(Dag dag, Cache cache) {
        // 1. Find all leaves
        // 2. set taskSet = leaves
        // 3. while !taskSet.empty:
        //      Node current = taskSet.next;
        //      future = execute(current);
        //      current.future = future;
        //      for Node child in current.dependents:
        //          if all child's parents are started
        //              taskSet.add(child)
        //
        // 2. for root in roots
        //  1. future = execute root
        //  2. get all dependents
        //  3. for dependent in dependents
        //      1. execute(dependent, future, cache);
        // 3. Profit
        Set<Dag.Node> nodeSet = dag.findRoots();
        Dag.Node current = null;
        while (!nodeSet.isEmpty()) {
            current = nodeSet.iterator().next();
            current.future = Optional.of(execute(current.task, dag, cache));
            for (Dag.Node<?> child : current.dependents) {
                if (dag.isReady(child)) {
                    nodeSet.add(child);
                }
            }
        }
        return current;
    }

    private <Res> CompletableFuture<Res> execute(RequestTask<Res> task) {
        Service service = context.get(task.request);
        return service.handle(request);
    }

    private <T, V> CompletableFuture<V> execute(MapTask<T, V> task, CompletableFuture<T> previous) {
        return previous.map(task.mapper);
    }

    private <T, V> CompletableFuture<V> execute(FlatMapTask<T, V> task, CompletableFuture<T> previous, Cache cache) {
        return previous.flatMap(value -> {
            Task<V> newTask = task.mapper(value);
            Dag dag = evaluate(newTask);
            return execute(dag, cache);
        });
    }

    class Cache {
        Map<Request<?>, CompletableFuture<?>> pendingRequests;
    }

    class Context {
        Map<Request, Service> services;
    }
}
