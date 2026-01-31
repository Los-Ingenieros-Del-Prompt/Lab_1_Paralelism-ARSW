package edu.eci.arsw.parallelism.core;

import edu.eci.arsw.parallelism.concurrency.ParallelStrategy;
import edu.eci.arsw.parallelism.monitoring.PerformanceMonitor;
import org.springframework.stereotype.Service;
import edu.eci.arsw.parallelism.monitoring.PiExecutionResult;
import java.util.List;

@Service
public class PiDigitsService {

    private final List<ParallelStrategy> strategies;
    private final PerformanceMonitor performanceMonitor;

    public PiDigitsService(List<ParallelStrategy> strategies, PerformanceMonitor performanceMonitor) {
        this.strategies = strategies;
        this.performanceMonitor = performanceMonitor;
    }

    public String calculateSequential(int start, int count) {
        if (start < 0) {
            throw new IllegalArgumentException("start must be >= 0");
        }
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        }
        if (count == 0) {
            return "";
        }
        return PiDigits.getDigitsHex(start, count);
    }

    public String calculate(
            int start,
            int count,
            Integer threads,
            String strategyName
    ) {

        if (strategyName == null || strategyName.equalsIgnoreCase("sequential")) {
            return calculateSequential(start, count);
        }

        ParallelStrategy strategy = strategies.stream()
                .filter(s -> s.name().equalsIgnoreCase(strategyName))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown strategy: " + strategyName));

        return calculateThreads(start, count, threads, strategy);
    }

    public String calculateThreads(
            int start,
            int count,
            Integer threads,
            ParallelStrategy strategy
    ) {
        int threadCount = (threads == null || threads <= 0)
                ? Runtime.getRuntime().availableProcessors()
                : threads;

        return strategy.calculate(start, count, threadCount);
    }


    public PiExecutionResult calculateWithTiming(int start, int count, Integer threads, String strategyName) {

        String usedStrategy = (strategyName == null || strategyName.equalsIgnoreCase("sequential"))
                ? "sequential"
                : strategyName;

        int usedThreads = usedStrategy.equals("sequential")
                ? 1
                : ((threads == null || threads <= 0)
                        ? Runtime.getRuntime().availableProcessors()
                        : threads);

        return performanceMonitor.measurePiCalculation(
                () -> calculate(start, count, threads, strategyName),
                usedStrategy,
                usedThreads
        );
    }

}
