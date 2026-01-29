
package edu.eci.arsw.parallelism.core;

import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

@Service
public class PiDigitsService {

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

    public String calculateWithThreads(@Min(0) int start, @Min(1) int count, int numThreads) {
        return "hola";
    }
}
