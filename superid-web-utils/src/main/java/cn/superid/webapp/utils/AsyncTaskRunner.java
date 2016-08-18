package cn.superid.webapp.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zoowii on 15/4/25.
 */
@Component
public class AsyncTaskRunner {
    private final ExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    public Future<?> run(Runnable task) {
        return executorService.submit(task);
    }
    public <V> Future<V> run(Callable<V> task) {
        return executorService.submit(task);
    }
}
