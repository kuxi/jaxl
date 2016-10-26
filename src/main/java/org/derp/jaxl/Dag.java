package org.derp.jaxl;

import com.google.common.reflect.TypeToken;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class Dag {

    private List<Node> nodes;

    Dag(Node n) {
        nodes = Collections.singletonList(n);
    }

    static <T> Dag withTask(Task<T> task) {
        return new Dag(new Node<T>(task));
    }

    boolean isReady(Node node) {
        for (Node parent : node.dependencies) {
            if (!parent.future.isPresent()) {
                return false;
            }
        }
        return true;
    }

    Set<Node> findRoots() {
        return nodes.stream()
            .filter(n -> n.dependents.isEmpty())
            .collect(Collectors.toSet());
    }

    <T> Node<T> lookup(Task<T> task) {
        // TODO: Implement me!
        return new Node<T>(task);
    }

    <T> void depend(Node<?> parent, Task<T> task) {
        Node child = new Node<>(task);
        child.dependents.add(parent);
        parent.dependencies.add(child);
        nodes.add(child);
    }

    static class Node<T> {
        Task<T> task;
        Set<Node<?>> dependents;
        Set<Node<?>> dependencies;
        Optional<CompletableFuture<T>> future;

        Node(Task<T> task) {
            this.task = task;
            dependents = new HashSet<>();
            dependencies = new HashSet<>();
            future = Optional.empty();
        }
    }
}
