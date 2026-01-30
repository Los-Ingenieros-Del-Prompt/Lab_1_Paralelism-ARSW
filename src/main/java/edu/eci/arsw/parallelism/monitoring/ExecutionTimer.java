package edu.eci.arsw.parallelism.monitoring;

import java.util.function.Supplier;

public class ExecutionTimer {

    public static <T> TimedResult<T> measure(Supplier<T> task) {
        long start = System.nanoTime();
        T result = task.get();
        long end = System.nanoTime();
        return new TimedResult<>(result, end - start);
    }
}
