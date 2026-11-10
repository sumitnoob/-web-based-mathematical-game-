package service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * All the mathematics used by SUMIT - MATH WHIZ.
 *
 * Every answer in the game is CALCULATED by one of these methods at the moment
 * the player submits - no answer is ever stored or hard-coded.
 */
public final class MathService {

    /** A decimal answer is accepted when it is within this distance of the real value. */
    public static final double TOLERANCE = 0.01;

    /** Marks for each question: Q1 prime, Q2 modulus, Q3 and Q4 trigonometry. */
    public static final int PRIME_MARKS = 2;
    public static final int MOD_MARKS = 2;
    public static final int TRIG_MARKS = 3;
    public static final int MAX_SCORE = PRIME_MARKS + MOD_MARKS + 2 * TRIG_MARKS; // 10

    private MathService() { }

    // ------------------------------------------------------------------
    //  TASK 03 - Prime number
    // ------------------------------------------------------------------

    /**
     * @return 1 if {@code number} is prime, otherwise 0.
     *         Numbers less than 2 (1, 0 and negatives) are not prime.
     */
    public static int isPrime(int number) {
        if (number < 2) {
            return 0;
        }
        if (number < 4) {          // 2 and 3
            return 1;
        }
        if (number % 2 == 0) {
            return 0;
        }
        // Only odd divisors up to the square root need to be tested.
        for (long i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return 0;
            }
        }
        return 1;
    }

    // ------------------------------------------------------------------
    //  TASK 04 - Modulus
    // ------------------------------------------------------------------

    /**
     * @return the remainder of a / b, calculated with the % operator.
     * @throws IllegalArgumentException if the divisor is zero (validation error)
     */
    public static int calculateMod(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Divisor must not be zero");
        }
        return a % b;
    }

    // ------------------------------------------------------------------
    //  TASK 05 - Trigonometry (angles are given in DEGREES)
    // ------------------------------------------------------------------

    /** Sine of an angle in degrees. */
    public static double calculateSin(double degree) {
        return Math.sin(Math.toRadians(degree));
    }

    /** Cosine of an angle in degrees. */
    public static double calculateCos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    /**
     * Tangent of an angle in degrees.
     *
     * @throws ArithmeticException at 90, 270, -90 ... degrees where tangent is undefined
     */
    public static double calculateTan(double degree) {
        if (!isTanDefined(degree)) {
            throw new ArithmeticException("Tangent is undefined at " + degree + " degrees");
        }
        return Math.tan(Math.toRadians(degree));
    }

    /** Tangent is undefined whenever cos(angle) = 0, i.e. 90 + k * 180 degrees. */
    public static boolean isTanDefined(double degree) {
        double d = ((degree % 180) + 180) % 180;   // bring into 0..180
        return Math.abs(d - 90) > 1e-9;
    }

    /** Calculates "sin", "cos" or "tan" for the given angle in degrees. */
    public static double calculateTrig(String function, double degree) {
        switch (function) {
            case "sin": return calculateSin(degree);
            case "cos": return calculateCos(degree);
            case "tan": return calculateTan(degree);
            default:    throw new IllegalArgumentException("Unknown trigonometric function: " + function);
        }
    }

    // ------------------------------------------------------------------
    //  Comparing and showing decimal answers
    // ------------------------------------------------------------------

    /** True when the player's decimal answer is within {@link #TOLERANCE} of the real answer. */
    public static boolean isClose(double guess, double answer) {
        return Math.abs(guess - answer) <= TOLERANCE;
    }

    /** Formats a result for display: 0.8660254 -> "0.866", 1.0000000000000002 -> "1", -1.8e-16 -> "0". */
    public static String format(double value) {
        BigDecimal bd = BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
        return bd.signum() == 0 ? "0" : bd.toPlainString();
    }

    // ------------------------------------------------------------------
    //  TASK 06 - Scoring logic (maximum 10 marks)
    // ------------------------------------------------------------------

    public static int totalScore(boolean primeCorrect, boolean modCorrect,
                                 boolean trig1Correct, boolean trig2Correct) {
        int totalScore = 0;

        if (primeCorrect) totalScore += PRIME_MARKS;
        if (modCorrect)   totalScore += MOD_MARKS;
        if (trig1Correct) totalScore += TRIG_MARKS;
        if (trig2Correct) totalScore += TRIG_MARKS;

        return totalScore;
    }
}
