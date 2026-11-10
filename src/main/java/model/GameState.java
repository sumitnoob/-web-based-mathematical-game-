package model;

import java.io.Serializable;

import service.MathService;

/**
 * Everything about ONE game of SUMIT - MATH WHIZ. It is kept in the player's
 * HTTP session on the server, so the player can never see or change the
 * question numbers or the answers.
 */
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String[] AREAS = {"Prime Number", "Modulus", "Trigonometry", "Trigonometry"};
    private static final int[] MAX_MARKS = {
        MathService.PRIME_MARKS, MathService.MOD_MARKS, MathService.TRIG_MARKS, MathService.TRIG_MARKS};

    // ---- the question numbers (chosen by the system) ----
    private String player;
    private int primeNumber;
    private int modA;
    private int modB;
    private String trigFn1;
    private int angle1;
    private String trigFn2;
    private int angle2;

    // ---- filled in when the answers are submitted ----
    private final String[] answers = {"", "", "", ""};    // what the player typed
    private final String[] expected = {"", "", "", ""};   // what Java calculated
    private final boolean[] correct = new boolean[4];
    private int totalScore;
    private boolean finished;
    private String playedAtText = "";

    // ------------------------------------------------------------------
    //  Text shown on the pages
    // ------------------------------------------------------------------

    public String getArea(int i) { return AREAS[i]; }

    public int getMaxMarks(int i) { return MAX_MARKS[i]; }

    public int getEarnedMarks(int i) { return correct[i] ? MAX_MARKS[i] : 0; }

    /** The "calculator display" text, e.g. isPrime(37), 47 % 6, sin(30°). */
    public String getExpression(int i) {
        switch (i) {
            case 0:  return "isPrime(" + primeNumber + ")";
            case 1:  return modA + " % " + modB;
            case 2:  return trigFn1 + "(" + angle1 + "\u00b0)";
            default: return trigFn2 + "(" + angle2 + "\u00b0)";
        }
    }

    public String getQuestionText(int i) {
        switch (i) {
            case 0:  return "Is " + primeNumber + " a prime number?";
            case 1:  return "What is the remainder of " + modA + " divided by " + modB + "?";
            case 2:  return "Calculate the " + fullName(trigFn1) + " of " + angle1 + " degrees.";
            default: return "Calculate the " + fullName(trigFn2) + " of " + angle2 + " degrees.";
        }
    }

    public String getHint(int i) {
        switch (i) {
            case 0:  return "Type 1 if it is prime, 0 if it is not prime.";
            case 1:  return "Whole numbers only (integer remainder).";
            default: return "Round to 2 decimal places, e.g. 0.87 (answers within 0.01 are accepted).";
        }
    }

    /** Name of the Java method that calculates question i, shown on the result page. */
    public String getMethodName(int i) {
        switch (i) {
            case 0:  return "MathService.isPrime(" + primeNumber + ")";
            case 1:  return "MathService.calculateMod(" + modA + ", " + modB + ")";
            case 2:  return "MathService.calculate" + capital(trigFn1) + "(" + angle1 + ")";
            default: return "MathService.calculate" + capital(trigFn2) + "(" + angle2 + ")";
        }
    }

    private static String fullName(String fn) {
        switch (fn) {
            case "sin": return "sine";
            case "cos": return "cosine";
            default:    return "tangent";
        }
    }

    private static String capital(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    // ------------------------------------------------------------------
    //  Getters and setters
    // ------------------------------------------------------------------

    public String getPlayer() { return player; }
    public void setPlayer(String player) { this.player = player; }

    public int getPrimeNumber() { return primeNumber; }
    public void setPrimeNumber(int primeNumber) { this.primeNumber = primeNumber; }

    public int getModA() { return modA; }
    public void setModA(int modA) { this.modA = modA; }

    public int getModB() { return modB; }
    public void setModB(int modB) { this.modB = modB; }

    public String getTrigFn1() { return trigFn1; }
    public void setTrigFn1(String trigFn1) { this.trigFn1 = trigFn1; }

    public int getAngle1() { return angle1; }
    public void setAngle1(int angle1) { this.angle1 = angle1; }

    public String getTrigFn2() { return trigFn2; }
    public void setTrigFn2(String trigFn2) { this.trigFn2 = trigFn2; }

    public int getAngle2() { return angle2; }
    public void setAngle2(int angle2) { this.angle2 = angle2; }

    public String[] getAnswers() { return answers; }
    public String[] getExpected() { return expected; }
    public boolean[] getCorrect() { return correct; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }

    public String getPlayedAtText() { return playedAtText; }
    public void setPlayedAtText(String playedAtText) { this.playedAtText = playedAtText; }
}
