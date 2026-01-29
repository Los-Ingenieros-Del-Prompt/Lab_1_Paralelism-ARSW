package edu.eci.arsw.parallelism.core;

import edu.eci.arsw.parallelism.concurrency.ParallelStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PiDigitsServiceTest {

    private final PiDigitsService service =
            new PiDigitsService(List.of());

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
}