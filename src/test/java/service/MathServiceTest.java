package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** TASK 03, 04, 05 and 09 - tests for the mathematical methods. */
class MathServiceTest {

    // ---------------- prime number ----------------
    @Test void primeInput7Returns1()  { assertEquals(1, MathService.isPrime(7)); }
    @Test void primeInput8Returns0()  { assertEquals(0, MathService.isPrime(8)); }
    @Test void primeInput1Returns0()  { assertEquals(0, MathService.isPrime(1)); }

    @Test
    void numbersLessThanTwoAreNotPrime() {
        assertEquals(0, MathService.isPrime(0));
        assertEquals(0, MathService.isPrime(-7));
    }

    @Test
    void smallAndLargePrimes() {
        int[] primes = {2, 3, 5, 11, 13, 97, 7919, 2147483647};
        for (int p : primes) {
            assertEquals(1, MathService.isPrime(p), p + " should be prime");
        }
        int[] composites = {4, 9, 15, 21, 25, 49, 91, 100, 7917};
        for (int c : composites) {
            assertEquals(0, MathService.isPrime(c), c + " should not be prime");
        }
    }

    // ---------------- modulus ----------------
    @Test void modulus17By5Is2() { assertEquals(2, MathService.calculateMod(17, 5)); }

    @Test
    void modulusWithNegativeAndZeroDividend() {
        assertEquals(-2, MathService.calculateMod(-17, 5));   // Java % keeps the sign of the dividend
        assertEquals(2, MathService.calculateMod(17, -5));
        assertEquals(0, MathService.calculateMod(0, 7));
        assertEquals(0, MathService.calculateMod(20, 5));
    }

    @Test
    void divisorZeroIsAValidationError() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> MathService.calculateMod(10, 0));
        assertEquals("Divisor must not be zero", ex.getMessage());
    }

    // ---------------- trigonometry ----------------
    @Test void sine30Is0Point5()    { assertEquals(0.5, MathService.calculateSin(30), 1e-9); }
    @Test void cosine60Is0Point5()  { assertEquals(0.5, MathService.calculateCos(60), 1e-9); }
    @Test void tangent45Is1()       { assertEquals(1.0, MathService.calculateTan(45), 1e-9); }

    @Test
    void otherAngles() {
        assertEquals(0.0, MathService.calculateSin(0), 1e-9);
        assertEquals(1.0, MathService.calculateSin(90), 1e-9);
        assertEquals(0.0, MathService.calculateSin(180), 1e-9);
        assertEquals(-1.0, MathService.calculateCos(180), 1e-9);
        assertEquals(1.0, MathService.calculateCos(360), 1e-9);
        assertEquals(Math.sqrt(3), MathService.calculateTan(60), 1e-9);
        assertEquals(-1.0, MathService.calculateTan(135), 1e-9);
    }

    @Test
    void pythagoreanIdentityHolds() {                       // x^2 + y^2 = 1  ->  cos^2 + sin^2 = 1
        for (int deg = -360; deg <= 720; deg += 15) {
            double s = MathService.calculateSin(deg);
            double c = MathService.calculateCos(deg);
            assertEquals(1.0, s * s + c * c, 1e-12, "angle " + deg);
        }
    }

    @Test
    void tangentIsUndefinedAt90Plus180k() {
        for (double d : new double[] {90, 270, -90, 450}) {
            assertFalse(MathService.isTanDefined(d));
            assertThrows(ArithmeticException.class, () -> MathService.calculateTan(d));
        }
        assertTrue(MathService.isTanDefined(45));
        assertTrue(MathService.isTanDefined(0));
    }

    @Test
    void calculateTrigPicksTheRightFunction() {
        assertEquals(0.5, MathService.calculateTrig("sin", 30), 1e-9);
        assertEquals(0.5, MathService.calculateTrig("cos", 60), 1e-9);
        assertEquals(1.0, MathService.calculateTrig("tan", 45), 1e-9);
        assertThrows(IllegalArgumentException.class, () -> MathService.calculateTrig("sec", 45));
    }

    // ---------------- tolerance / formatting ----------------
    @Test
    void toleranceAcceptsRoundedAnswers() {
        double sin60 = MathService.calculateSin(60);        // 0.8660254...
        assertTrue(MathService.isClose(0.87, sin60));
        assertTrue(MathService.isClose(0.866, sin60));
        assertFalse(MathService.isClose(0.9, sin60));
        assertFalse(MathService.isClose(0.5, sin60));
    }

    @Test
    void formatHidesFloatingPointNoise() {
        assertEquals("0", MathService.format(MathService.calculateSin(180)));   // 1.2e-16
        assertEquals("1", MathService.format(MathService.calculateTan(45)));    // 0.9999999999999999
        assertEquals("0.866", MathService.format(MathService.calculateSin(60)));
        assertEquals("-0.5", MathService.format(MathService.calculateCos(120)));
    }
}
