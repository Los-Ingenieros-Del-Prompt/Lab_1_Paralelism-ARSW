package edu.eci.arsw.parallelism.concurrency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class ThreadJoinStrategyTest {

    private ThreadJoinStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ThreadJoinStrategy();
    }

    @Test
    void testName() {
        assertEquals("threads", strategy.name());
    }

    @Test
    void testCalculateWithSingleThread() {
        String result = strategy.calculate(0, 5, 1);
        assertNotNull(result);
        assertEquals(5, result.length());
        assertEquals("243F6", result);
    }

    @Test
    void testCalculateWithMultipleThreads() {
        String result = strategy.calculate(0, 10, 4);
        assertNotNull(result);
        assertEquals(10, result.length());
        assertTrue(result.matches("[0-9A-F]+"));
    }

    @Test
    void testCalculateConsistencyWithSequential() {
        String sequential = edu.eci.arsw.parallelism.core.PiDigits.getDigitsHex(0, 10);
        String parallel = strategy.calculate(0, 10, 4);
        assertEquals(sequential, parallel);
    }

    @Test
    void testCalculateWithMoreThreadsThanDigits() {
        String result = strategy.calculate(0, 5, 10);
        assertNotNull(result);
        assertEquals(5, result.length());
        assertEquals("243F6", result);
    }

    @Test
    void testCalculateWithTwoThreads() {
        String result = strategy.calculate(0, 10, 2);
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    @Test
    void testCalculateWithFourThreads() {
        String result = strategy.calculate(0, 20, 4);
        assertNotNull(result);
        assertEquals(20, result.length());
    }

    @Test
    void testCalculateLargeCountWithMultipleThreads() {
        String result = strategy.calculate(0, 100, 8);
        assertNotNull(result);
        assertEquals(100, result.length());
        assertTrue(result.matches("[0-9A-F]+"));
    }

    @Test
    void testCalculateWithDifferentStart() {
        String result = strategy.calculate(10, 5, 2);
        assertNotNull(result);
        assertEquals(5, result.length());
    }

    @Test
    void testCalculateResultsAreConsistent() {
        String result1 = strategy.calculate(0, 10, 4);
        String result2 = strategy.calculate(0, 10, 4);
        assertEquals(result1, result2);
    }

    @Test
    void testCalculateWithOddNumberOfDigits() {
        String result = strategy.calculate(0, 11, 4);
        assertNotNull(result);
        assertEquals(11, result.length());
    }

    @Test
    void testCalculateCorrectSegmentation() {
        String sequential = edu.eci.arsw.parallelism.core.PiDigits.getDigitsHex(0, 13);
        String parallel = strategy.calculate(0, 13, 5);
        assertEquals(sequential, parallel);
    }

    @Test
    void testCalculateWithOneDigit() {
        String result = strategy.calculate(0, 1, 2);
        assertNotNull(result);
        assertEquals(1, result.length());
        assertEquals("2", result);
    }

    @Test
    void testCalculateVerifyAllDigitsValid() {
        String result = strategy.calculate(0, 50, 4);
        assertTrue(result.matches("[0-9A-F]+"));
    }

    @Test
    void testCalculateDifferentThreadCounts() {
        String result2 = strategy.calculate(0, 20, 2);
        String result4 = strategy.calculate(0, 20, 4);
        String result8 = strategy.calculate(0, 20, 8);

        assertEquals(result2, result4);
        assertEquals(result4, result8);
    }

    @Test
    void testCalculateWithLargeStart() {
        String result = strategy.calculate(100, 10, 4);
        assertNotNull(result);
        assertEquals(10, result.length());
    }
}