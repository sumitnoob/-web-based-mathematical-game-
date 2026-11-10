package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import model.GameState;

/** TASK 06 and 09 - game logic and scoring. */
class GameServiceTest {

    private final GameService service = new GameService(new Random(42));

    /** The correct answers, worked out with the same MathService methods. */
    private static String[] correctAnswers(GameState g) {
        return new String[] {
            String.valueOf(MathService.isPrime(g.getPrimeNumber())),
            String.valueOf(MathService.calculateMod(g.getModA(), g.getModB())),
            String.format("%.2f", MathService.calculateTrig(g.getTrigFn1(), g.getAngle1())),
            String.format("%.2f", MathService.calculateTrig(g.getTrigFn2(), g.getAngle2()))
        };
    }

    @Test
    void allAnswersCorrectGivesMaximum10Marks() {
        for (int i = 0; i < 200; i++) {                       // 200 different random games
            GameState g = service.newGame("Sumit");
            String[] answers = correctAnswers(g);
            assertTrue(GameService.validate(answers).isEmpty());
            service.evaluate(g, answers);
            assertEquals(10, g.getTotalScore());
        }
    }

    @Test
    void allAnswersWrongGivesZero() {
        GameState g = service.newGame("Sumit");
        String[] wrong = {
            String.valueOf(1 - MathService.isPrime(g.getPrimeNumber())),
            String.valueOf(MathService.calculateMod(g.getModA(), g.getModB()) + 1),
            "99", "99"
        };
        service.evaluate(g, wrong);
        assertEquals(0, g.getTotalScore());
    }

    @Test
    void wrongAnswersLoseOnlyTheirOwnMarks() {
        GameState g = service.newGame("Sumit");

        String[] a = correctAnswers(g);
        a[0] = String.valueOf(1 - MathService.isPrime(g.getPrimeNumber()));    // Q1 wrong  (-2)
        service.evaluate(g, a);
        assertEquals(8, g.getTotalScore());
        assertFalse(g.getCorrect()[0]);

        a = correctAnswers(g);
        a[1] = String.valueOf(MathService.calculateMod(g.getModA(), g.getModB()) + 1);   // Q2 wrong (-2)
        service.evaluate(g, a);
        assertEquals(8, g.getTotalScore());

        a = correctAnswers(g);
        a[2] = "123";                                                          // Q3 wrong  (-3)
        service.evaluate(g, a);
        assertEquals(7, g.getTotalScore());

        a = correctAnswers(g);
        a[3] = "123";                                                          // Q4 wrong  (-3)
        service.evaluate(g, a);
        assertEquals(7, g.getTotalScore());
    }

    @Test
    void scoringFormulaMatchesTheAssignment() {
        assertEquals(10, MathService.totalScore(true, true, true, true));
        assertEquals(0, MathService.totalScore(false, false, false, false));
        assertEquals(2, MathService.totalScore(true, false, false, false));
        assertEquals(2, MathService.totalScore(false, true, false, false));
        assertEquals(3, MathService.totalScore(false, false, true, false));
        assertEquals(6, MathService.totalScore(false, false, true, true));
        assertEquals(MathService.MAX_SCORE, 10);
    }

    @Test
    void everyGameHasOnePrimeOneModAndTwoDifferentTrigQuestions() {
        for (int i = 0; i < 500; i++) {
            GameState g = service.newGame("Sumit");
            assertTrue(g.getPrimeNumber() >= 1 && g.getPrimeNumber() <= 100);
            assertNotEquals(0, g.getModB(), "divisor must never be zero");
            assertNotEquals(g.getTrigFn1(), g.getTrigFn2());
            if (g.getTrigFn1().equals("tan")) assertTrue(MathService.isTanDefined(g.getAngle1()));
            if (g.getTrigFn2().equals("tan")) assertTrue(MathService.isTanDefined(g.getAngle2()));
        }
    }

    @Test
    void validationAcceptsGoodInput() {
        assertTrue(GameService.validate(new String[] {"1", "-3", "0.87", "0,5"}).isEmpty());
        assertTrue(GameService.validate(new String[] {" 0 ", "12", "-1", ".5"}).isEmpty());
    }

    @Test
    void validationRejectsBadInput() {
        List<String> errors = GameService.validate(new String[] {"2", "3.5", "abc", ""});
        assertEquals(4, errors.size());

        assertEquals(1, GameService.validate(new String[] {"yes", "3", "0.5", "0.5"}).size());
        assertEquals(1, GameService.validate(new String[] {"1", "", "0.5", "0.5"}).size());
        assertEquals(1, GameService.validate(new String[] {"1", "3", "NaN", "0.5"}).size());
        assertEquals(1, GameService.validate(new String[] {"1", "3", "0.5", "1e5"}).size());
        assertEquals(4, GameService.validate(new String[] {null, null, null, null}).size());
    }
}
