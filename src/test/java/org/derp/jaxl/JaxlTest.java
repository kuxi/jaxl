package org.derp.jaxl;

import com.google.common.reflect.TypeToken;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public final class JaxlTest {
    @Test public void superBasicTest() throws Exception {
        Jaxl jaxl = new Jaxl();
        UserService userService = new UserService();
        jaxl.register(TypeToken.of(UserService.GetUserRequest.class), userService);
        final Task<User> user = UserService.getUser("leswen");
        final CompletableFuture<User> result = jaxl.run(user);
        Assert.assertEquals(result.get(), UserService.mockUser);

    }

    static class UserService implements Service {
        public static User mockUser = new User();

        @Override
        public <Req extends Request<Res>, Res> CompletableFuture<Res> getResult(Req request) {
            if (request instanceof GetUserRequest) {
                return CompletableFuture.completedFuture(mockUser);
            } else {
                throw new RuntimeException("Service not implemented for request type");
            }
        }

        static Task<User> getUser(String id) {
            return new RequestTask<>(new GetUserRequest(id));
        }

        static final class GetUserRequest implements Request<User> {
            final String userId;
            GetUserRequest(String userId) {
                this.userId = userId;
            }
        }

    }

    @Test public void basicTest() throws Exception {
        // TODO: Define User service
        // TODO: Define Posts service
        // TODO: Define Experiment service
        // TODO: Define Lockdown service
        // TODO: Define getUserById User request
        // TODO: Define getPostsforUser Postss request
        // TODO: Define getExperimentForUser Experiment request
        // TODO: Define isLockedDown Lockdown request
        // TODO: register User, Posts, Experiment services with jaxl
        
//        String userId = "some id";
//        String experiment = "can read posts";
//
//        Task<Boolean> accessCheck = getUser(userId)
//            .flatMap(user -> {
//                evaluateExperiment(user, experiment)
//                    .join(isLockedDown(user))
//                    .merge((canRead, isLocked) -> canRead && !isLocked);
//            });
//        accessCheck.flatMap(hasAccess -> {
//                if (hasAccess) {
//                    return Result.ok(getPosts(user));
//                } else {
//                    return Result.error(fail("u don't have access"));
//                }
//            });
//
//        Future<ImmutableList<Post>> postsFuture = Jaxl.run(postTask);
//
//        ImmutableList<Post> posts = postsFuture.get();
//
//        // Assert that posts matches what dummy posts service returns
    }

    Task<ImmutableList<Post>> getPosts(User user) {
        return new RequestTask<ImmutableList<Post>>(new GetPosts(user));
    }

    Task<Boolean> evaluateExperiment(User user, String experiment) {
        return new RequestTask<Boolean>(new EvaluateExperiment(user, experiment));
    }

    Task<Boolean> isLockedDown(User user) {
        return new RequestTask<Boolean>(new LockedDown(user));
    }

    Task<Error> fail(String message) {
        return new ImmediateTask<Error>(new Error(message));
    }

    static final class User {
    }
    final class Post {
    }
    final class Error {
        final String message;
        Error(String message) {
            this.message = message;
        }
    }

    final static class Result<T, E> {
        final Optional<T> value;
        final Optional<E> error;

        private Result(Optional<T> value, Optional<E> error) {
            this.value = value;
            this.error = error;
        }

        public static <T, E> Result<T, E> ok(T value) {
            return new Result<T, E>(Optional.of(value), Optional.empty());
        }

        public static <T, E> Result<T, E> error(E error) {
            return new Result<T, E>(Optional.empty(), Optional.of(error));
        }
    }

    final class EvaluateExperiment implements Request<Boolean> {
        final User user;
        final String experiment;
        EvaluateExperiment(User user, String experiment) {
            this.user = user;
            this.experiment = experiment;
        }
    }

    final class GetPosts implements Request<ImmutableList<Post>> {
        final User user;
        GetPosts(User user) {
            this.user = user;
        }
    }

    final class LockedDown implements Request<Boolean> {
        final User user;
        LockedDown(User user) {
            this.user = user;
        }
    }
}
