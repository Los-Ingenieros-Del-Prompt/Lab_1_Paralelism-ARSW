package edu.eci.arsw.parallelism.monitoring;

public record TimedResult<T>(T result, long timeNanos) {
    public double timeMillis() {
        return timeNanos / 1_000_000.0;
    }
}
