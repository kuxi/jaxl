package org.derp.jaxl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import org.derp.jaxl.Jaxl.Dag;

public final class Jaxl {
    Context context;

    public void register(Request request, Service service) {
    }

    public static <T> Task<T> request(Request<T> request) {
        return new RequestTask<T>(request);
    }

    public <T> Future<T> run(Task<T> task) {
        // GET (DAG, Cache)
        Dag state = evaluate(task);
        Cache cache = new Cache();
        return execute(dag, cache);
    }

    private <T> Dag evaluate(RequestTask<T> task) {
        return new Dag(task);
    }

    private <T> Dag evaluate(ImmediateTask<T> task) {
        return new Dag(task);
    }

    private <T> Dag evaluate(MapTask<T> task) {
        Dag dag = evaluate(task.parent);
        return dag.depend(dag.lookup(task.parent), task);
    }

    private <T> Future<T> execute(Dag, Cache) {
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
        Set<Node> nodeSet = dag.getLeaves();
        Node current = null;
        while (!nodeSet.empty()) {
            current = nodeSet.next();
            Future<?> future = execute(current.task, dag, cache);
            current.future = future;
            for (Node child : current.dependents) {
                if (dag.isReady(child)) {
                    nodeSet.add(child);
                }
            }
        }
        return current;
    }

    private <T> Future<T> execute(RequestTask<T> task) {
        Service service = context.get(task.request);
        return service.handle(request);
    }

    private <T, V> Future<V> execute(MapTask<T, V> task, Future<T> previous) {
        return previous.map(task.transformer);
    }

    private <T, V> Future<V> execute(FlatMapTask<T, V> task, Future<T> previous, Cache cache) {
        return previous.flatMap(value -> {
            Task<V> newTask = task.transformer(value);
            Dag dag = evaluate(newTask);
            return execute(dag, cache);
        });
    }

    class Dag {
        List<Node> nodes;

        boolean isReady(Node node) {
            for (Node parent : node.dependencies) {
                if (parent.future.isAbsent()) {
                    return false;
                }
            }
            return true;
        }
    }

    class Cache {
        Map<Request<?>, Future<?>> pendingRequests;
    }

    class Service {
    }
    class Node<T> {
        Task<T> task;
        List<Task<?>> dependents;
        List<Task<?>> dependencies;
        Optional<Future<T>> future;

        Node(Task<T> task) {
            this.task = task;
            dependents = new List();
            dependencies = new List();
            future = Optional.empty();
        }
    }

    class Context {
        Map<Request, Service> services;
    }
}
