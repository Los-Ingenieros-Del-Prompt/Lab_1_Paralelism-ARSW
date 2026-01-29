package edu.eci.arsw.parallelism.core;

import edu.eci.arsw.parallelism.concurrency.ParallelStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PiDigitsService {

    private final List<ParallelStrategy> strategies;

    public PiDigitsService(List<ParallelStrategy> strategies) {
        this.strategies = strategies;
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
}
