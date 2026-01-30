package edu.eci.arsw.parallelism.monitoring;

public record PiExecutionResult(
        String digits,
        String strategy,
        int threads,
        double timeMillis
) {}
