package edu.eci.arsw.parallelism.concurrency;

import edu.eci.arsw.parallelism.core.PiDigits;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
public class ThreadJoinStrategy implements ParallelStrategy {

    @Override
    public String calculate(int start, int count, int threads) {

        int actualThreads = Math.min(threads, count);

        String[] partialResults = new String[actualThreads];
        List<Thread> threadList = new ArrayList<>();

        int baseSize = count / actualThreads;
        int remainder = count % actualThreads;

        int currentStart = start;

        for (int i = 0; i < actualThreads; i++) {
            int segmentSize = baseSize + (i < remainder ? 1 : 0);
            int segmentStart = currentStart;
            int index = i;

            Thread thread = new Thread(() -> {
                // MISMA lógica que el secuencial
                partialResults[index] =
                        PiDigits.getDigitsHex(segmentStart, segmentSize);
            });

            threadList.add(thread);
            thread.start();

            currentStart += segmentSize;
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", e);
            }
        }

        StringBuilder result = new StringBuilder();
        for (String part : partialResults) {
            result.append(part);
        }

        return result.toString();
    }

    @Override
    public String name() {
        return "threads";
    }
}
