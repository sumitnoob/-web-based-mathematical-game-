package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import model.GameState;

/**
 * TASK 06 - Game logic. Creates the four questions (one prime, one modulus,
 * two trigonometric), validates the player's answers and evaluates them by
 * calling the {@link MathService} methods.
 */
public class GameService {

    /** At this stage every question has the same difficulty. It is stored with every score. */
    public static final String DIFFICULTY = "NORMAL";

    private static final int[] ANGLES = {0, 30, 45, 60, 90, 120, 135, 150, 180,
                                         210, 225, 240, 270, 300, 315, 330, 360};
    private static final String[] TRIG_FUNCTIONS = {"sin", "cos", "tan"};

    private static final Pattern INTEGER = Pattern.compile("[+-]?\\d{1,9}");
    private static final Pattern DECIMAL = Pattern.compile("[+-]?(\\d{1,9}(\\.\\d{0,9})?|\\.\\d{1,9})");

    private final Random random;

    public GameService() {
        this(new Random());
    }

    /** A seeded Random can be passed in for repeatable tests. */
    public GameService(Random random) {
        this.random = random;
    }

    // ------------------------------------------------------------------
    //  Creating a game
    // ------------------------------------------------------------------

    public GameState newGame(String player) {
        GameState g = new GameState();
        g.setPlayer(player);

        // Q1 - prime number: roughly half the games get a prime, half do not
        boolean wantPrime = random.nextBoolean();
        int n;
        do {
            n = random.nextInt(100) + 1;                       // 1..100
        } while ((MathService.isPrime(n) == 1) != wantPrime);
        g.setPrimeNumber(n);

        // Q2 - modulus: integers only, the divisor is never zero
        g.setModA(random.nextInt(90) + 10);                    // 10..99
        g.setModB(random.nextInt(11) + 2);                     // 2..12

        // Q3 and Q4 - two DIFFERENT trigonometric functions, tangent never at an undefined angle
        int first = random.nextInt(3);
        int second = (first + 1 + random.nextInt(2)) % 3;
        g.setTrigFn1(TRIG_FUNCTIONS[first]);
        g.setAngle1(pickAngle(TRIG_FUNCTIONS[first]));
        g.setTrigFn2(TRIG_FUNCTIONS[second]);
        g.setAngle2(pickAngle(TRIG_FUNCTIONS[second]));
        return g;
    }

    private int pickAngle(String function) {
        int angle;
        do {
            angle = ANGLES[random.nextInt(ANGLES.length)];
        } while (function.equals("tan") && !MathService.isTanDefined(angle));
        return angle;
    }

    // ------------------------------------------------------------------
    //  Validating the answers (before anything is scored or saved)
    // ------------------------------------------------------------------

    /** @return a list of error messages - empty when every answer is acceptable. */
    public static List<String> validate(String[] answers) {
        List<String> errors = new ArrayList<>();

        String q1 = clean(answers, 0);
        if (!INTEGER.matcher(q1).matches() || (Integer.parseInt(q1) != 0 && Integer.parseInt(q1) != 1)) {
            errors.add("Question 1: enter 1 (prime) or 0 (not prime).");
        }
        if (!INTEGER.matcher(clean(answers, 1)).matches()) {
            errors.add("Question 2: the remainder must be a whole number (integers only).");
        }
        if (parseDecimal(clean(answers, 2)) == null) {
            errors.add("Question 3: enter a number, for example 0.87.");
        }
        if (parseDecimal(clean(answers, 3)) == null) {
            errors.add("Question 4: enter a number, for example 0.87.");
        }
        return errors;
    }

    private static String clean(String[] answers, int i) {
        return (answers == null || i >= answers.length || answers[i] == null) ? "" : answers[i].trim();
    }

    /** Parses "0.87" or "0,87"; returns null when the text is not a plain decimal number. */
    static Double parseDecimal(String text) {
        String t = text.trim().replace(',', '.');
        return DECIMAL.matcher(t).matches() ? Double.valueOf(t) : null;
    }

    // ------------------------------------------------------------------
    //  Evaluating the answers
    // ------------------------------------------------------------------

    /**
     * Calculates the real answers with the MathService methods, compares them
     * with the player's answers and stores the score in the game.
     * The answers must already have passed {@link #validate(String[])}.
     */
    public void evaluate(GameState g, String[] answers) {
        // 1. the REAL answers - calculated now by Java methods
        int primeAnswer = MathService.isPrime(g.getPrimeNumber());
        int modAnswer = MathService.calculateMod(g.getModA(), g.getModB());
        double trig1Answer = MathService.calculateTrig(g.getTrigFn1(), g.getAngle1());
        double trig2Answer = MathService.calculateTrig(g.getTrigFn2(), g.getAngle2());

        g.getExpected()[0] = String.valueOf(primeAnswer);
        g.getExpected()[1] = String.valueOf(modAnswer);
        g.getExpected()[2] = MathService.format(trig1Answer);
        g.getExpected()[3] = MathService.format(trig2Answer);

        // 2. the player's answers
        for (int i = 0; i < 4; i++) {
            g.getAnswers()[i] = clean(answers, i);
        }

        // 3. compare (decimal answers use a tolerance)
        boolean primeCorrect = Integer.parseInt(g.getAnswers()[0]) == primeAnswer;
        boolean modCorrect = Integer.parseInt(g.getAnswers()[1]) == modAnswer;
        boolean trig1Correct = MathService.isClose(parseDecimal(g.getAnswers()[2]), trig1Answer);
        boolean trig2Correct = MathService.isClose(parseDecimal(g.getAnswers()[3]), trig2Answer);

        g.getCorrect()[0] = primeCorrect;
        g.getCorrect()[1] = modCorrect;
        g.getCorrect()[2] = trig1Correct;
        g.getCorrect()[3] = trig2Correct;

        // 4. total score (maximum 10)
        g.setTotalScore(MathService.totalScore(primeCorrect, modCorrect, trig1Correct, trig2Correct));
    }
}
