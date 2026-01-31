package edu.eci.arsw.parallelism.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PiDigitsTest {

    @Test
    void testGetDigitsValidInput() {
        byte[] digits = PiDigits.getDigits(0, 5);
        assertNotNull(digits);
        assertEquals(5, digits.length);
        for (byte d : digits) {
            assertTrue(d >= 0 && d <= 15);
        }
    }

    @Test
    void testGetDigitsHexValidInput() {
        String hex = PiDigits.getDigitsHex(0, 5);
        assertNotNull(hex);
        assertEquals(5, hex.length());
        assertTrue(hex.matches("[0-9A-F]+"));
    }

    @Test
    void testGetDigitsNegativeStart() {
        assertThrows(IllegalArgumentException.class, () -> PiDigits.getDigits(-1, 5));
    }

    @Test
    void testGetDigitsNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> PiDigits.getDigits(0, -1));
    }

    @Test
    void testGetDigitsZeroCount() {
        byte[] digits = PiDigits.getDigits(0, 0);
        assertNotNull(digits);
        assertEquals(0, digits.length);
    }

    @Test
    void testGetDigitsLargeStart() {
        byte[] digits = PiDigits.getDigits(100, 5);
        assertNotNull(digits);
        assertEquals(5, digits.length);
    }

    @Test
    void testGetDigitsHexReturnsUppercase() {
        String hex = PiDigits.getDigitsHex(0, 10);
        assertNotNull(hex);
        for (char c : hex.toCharArray()) {
            assertTrue(Character.isUpperCase(c) || Character.isDigit(c));
        }
    }

    @Test
    void testGetDigitsHexZeroCount() {
        String hex = PiDigits.getDigitsHex(0, 0);
        assertNotNull(hex);
        assertEquals(0, hex.length());
    }

    @Test
    void testGetDigitsHexNegativeStart() {
        assertThrows(IllegalArgumentException.class, () -> PiDigits.getDigitsHex(-1, 5));
    }

    @Test
    void testGetDigitsHexNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> PiDigits.getDigitsHex(0, -1));
    }

    @Test
    void testGetDigitsConsistency() {
        byte[] digits1 = PiDigits.getDigits(0, 5);
        byte[] digits2 = PiDigits.getDigits(0, 5);
        assertArrayEquals(digits1, digits2);
    }

    @Test
    void testGetDigitsHexConsistency() {
        String hex1 = PiDigits.getDigitsHex(0, 5);
        String hex2 = PiDigits.getDigitsHex(0, 5);
        assertEquals(hex1, hex2);
    }

    @Test
    void testGetDigitsKnownValue() {
        String hex = PiDigits.getDigitsHex(0, 1);
        assertEquals("2", hex);
    }

    @Test
    void testGetDigitsKnownSequence() {
        String hex = PiDigits.getDigitsHex(0, 5);
        assertEquals("243F6", hex);
    }

    @Test
    void testGetDigitsLargeCount() {
        byte[] digits = PiDigits.getDigits(0, 100);
        assertNotNull(digits);
        assertEquals(100, digits.length);
        for (byte d : digits) {
            assertTrue(d >= 0 && d <= 15);
        }
    }

    @Test
    void testGetDigitsHexLargeCount() {
        String hex = PiDigits.getDigitsHex(0, 100);
        assertNotNull(hex);
        assertEquals(100, hex.length());
        assertTrue(hex.matches("[0-9A-F]+"));
    }

    @Test
    void testGetDigitsDifferentStarts() {
        String hex1 = PiDigits.getDigitsHex(0, 5);
        String hex2 = PiDigits.getDigitsHex(5, 5);
        assertNotEquals(hex1, hex2);
    }

    @Test
    void testGetDigitsAllBytesValid() {
        byte[] digits = PiDigits.getDigits(0, 50);
        for (byte d : digits) {
            int value = d & 0xFF;
            assertTrue(value >= 0 && value <= 15,
                    "Digit value should be between 0 and 15, got: " + value);
        }
    }

    @Test
    void testGetDigitsVeryLargeStart() {
        byte[] digits = PiDigits.getDigits(1000, 5);
        assertNotNull(digits);
        assertEquals(5, digits.length);
    }

    @Test
    void testGetDigitsHexVeryLargeStart() {
        String hex = PiDigits.getDigitsHex(1000, 5);
        assertNotNull(hex);
        assertEquals(5, hex.length());
    }
}