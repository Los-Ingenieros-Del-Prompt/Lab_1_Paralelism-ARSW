package edu.eci.arsw.parallelism.monitoring;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class PerformanceMonitor {

    /**
     * Executes a Pi calculation and measures its execution time.
     *
     * @param calculationTask The calculation to execute
     * @param strategy The strategy name used
     * @param threads The number of threads used
     * @return Result with timing metrics
     */
    public PiExecutionResult measurePiCalculation(
            Supplier<String> calculationTask,
            String strategy,
            int threads
    ) {
        long startTime = System.nanoTime();
        
        String result = calculationTask.get();
        
        long endTime = System.nanoTime();
        double timeMillis = (endTime - startTime) / 1_000_000.0;

        return new PiExecutionResult(result, strategy, threads, timeMillis);
    }
}
