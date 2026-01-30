package edu.eci.arsw.parallelism.core;

import edu.eci.arsw.parallelism.concurrency.ParallelStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PiDigitsServiceTest {

    private PiDigitsService service;
    private ParallelStrategy mockStrategy;

    @BeforeEach
    void setUp() {
        mockStrategy = mock(ParallelStrategy.class);
        when(mockStrategy.name()).thenReturn("threads");
        service = new PiDigitsService(List.of(mockStrategy));
    }

    @Test
    void testCalculateSequential() {
        String result = service.calculateSequential(0, 5);
        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[0-9A-F]+"));
    }

    @Test
    void testCalculateSequentialZeroCount() {
        String result = service.calculateSequential(0, 0);
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test
    void testCalculateSequentialNegativeStart() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.calculateSequential(-1, 5);
        });
    }

    @Test
    void testCalculateSequentialNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.calculateSequential(0, -1);
        });
    }

    @Test
    void testCalculateSequentialLargeStart() {
        String result = service.calculateSequential(100, 5);
        assertNotNull(result);
        assertEquals(5, result.length());
    }

    @Test
    void testCalculateWithNullStrategy() {
        String result = service.calculate(0, 5, null, null);
        assertNotNull(result);
        assertEquals(5, result.length());
    }

    @Test
    void testCalculateWithSequentialStrategyName() {
        String result = service.calculate(0, 5, null, "sequential");
        assertNotNull(result);
        assertEquals(5, result.length());
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCalculateWithThreadsStrategyNoDeadlock() {
        when(mockStrategy.calculate(0, 5, 4)).thenReturn("243F6");

        String result = service.calculate(0, 5, 4, "threads");

        assertEquals("243F6", result);
        verify(mockStrategy).calculate(0, 5, 4);
    }

    @Test
    void testCalculateWithUnknownStrategy() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.calculate(0, 5, 4, "unknown");
        });
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCalculateThreadsWithNullThreadsNoDeadlock() {
        int expectedThreads = Runtime.getRuntime().availableProcessors();
        when(mockStrategy.calculate(eq(0), eq(5), anyInt())).thenReturn("243F6");

        String result = service.calculateThreads(0, 5, null, mockStrategy);

        assertNotNull(result);
        verify(mockStrategy).calculate(eq(0), eq(5), eq(expectedThreads));
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCalculateThreadsWithZeroThreadsNoDeadlock() {
        int expectedThreads = Runtime.getRuntime().availableProcessors();
        when(mockStrategy.calculate(eq(0), eq(5), anyInt())).thenReturn("243F6");

        String result = service.calculateThreads(0, 5, 0, mockStrategy);

        assertNotNull(result);
        verify(mockStrategy).calculate(eq(0), eq(5), eq(expectedThreads));
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCalculateThreadsWithSpecificThreadsNoDeadlock() {
        when(mockStrategy.calculate(0, 5, 8)).thenReturn("243F6");

        String result = service.calculateThreads(0, 5, 8, mockStrategy);

        assertEquals("243F6", result);
        verify(mockStrategy).calculate(0, 5, 8);
    }

    @Test
    void testCalculateWithCaseInsensitiveStrategyName() {
        when(mockStrategy.calculate(0, 5, 4)).thenReturn("243F6");

        String result = service.calculate(0, 5, 4, "THREADS");

        assertEquals("243F6", result);
    }

    @Test
    void testCalculateWithEmptyStrategyNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.calculate(0, 5, null, "");
        });
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSequentialVsParallelEquivalence() {
        PiDigitsService realService = new PiDigitsService(
                List.of(new edu.eci.arsw.parallelism.concurrency.ThreadJoinStrategy())
        );

        String sequential = realService.calculate(0, 20, null, "sequential");
        String parallel = realService.calculate(0, 20, 4, "threads");

        assertEquals(sequential, parallel, "Sequential and parallel should produce same results");
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testDeterminismMultipleRuns() {
        PiDigitsService realService = new PiDigitsService(
                List.of(new edu.eci.arsw.parallelism.concurrency.ThreadJoinStrategy())
        );

        String result1 = realService.calculate(0, 30, 4, "threads");
        String result2 = realService.calculate(0, 30, 4, "threads");
        String result3 = realService.calculate(0, 30, 4, "threads");

        assertEquals(result1, result2, "Results should be deterministic");
        assertEquals(result2, result3, "Results should be deterministic");
    }

    @Test
    void testCalculateWithTimingSequential() {
        var result = service.calculateWithTiming(0, 5, null, "sequential");
        
        assertNotNull(result);
        assertEquals("sequential", result.strategy());
        assertEquals(1, result.threads());
        assertEquals(5, result.digits().length());
        assertTrue(result.timeMillis() >= 0);
    }

    @Test
    void testCalculateWithTimingSequentialWithNullStrategy() {
        var result = service.calculateWithTiming(0, 5, null, null);
        
        assertNotNull(result);
        assertEquals("sequential", result.strategy());
        assertEquals(1, result.threads());
        assertEquals(5, result.digits().length());
        assertTrue(result.timeMillis() >= 0);
    }

    @Test
    void testCalculateWithTimingParallel() {
        when(mockStrategy.calculate(0, 5, 4)).thenReturn("243F6");
        
        var result = service.calculateWithTiming(0, 5, 4, "threads");
        
        assertNotNull(result);
        assertEquals("threads", result.strategy());
        assertEquals(4, result.threads());
        assertEquals("243F6", result.digits());
        assertTrue(result.timeMillis() >= 0);
    }

    @Test
    void testCalculateWithTimingParallelDefaultThreads() {
        int expectedThreads = Runtime.getRuntime().availableProcessors();
        when(mockStrategy.calculate(eq(0), eq(5), eq(expectedThreads))).thenReturn("243F6");
        
        var result = service.calculateWithTiming(0, 5, null, "threads");
        
        assertNotNull(result);
        assertEquals("threads", result.strategy());
        assertEquals(expectedThreads, result.threads());
        assertTrue(result.timeMillis() >= 0);
    }
}