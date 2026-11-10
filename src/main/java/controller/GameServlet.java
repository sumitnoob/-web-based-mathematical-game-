package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ScoreDAO;
import model.GameScore;
import model.GameState;
import service.GameService;
import service.MathService;

/**
 * The front controller of SUMIT - MATH WHIZ.
 *
 *   GET  /game          Screen 2 - the four questions
 *   POST /game          action=start  -> create a new game  |  action=submit -> evaluate + save score
 *   GET  /result        Screen 3 - final score
 *   GET  /leaderboard   Screen 4 - top 10 scores
 *   GET  /lab           Math Lab - try each Java method with your own numbers
 */
@WebServlet(urlPatterns = {"/game", "/result", "/leaderboard", "/lab"})
public class GameServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int LEADERBOARD_SIZE = 10;
    private static final int MAX_NAME_LENGTH = 50;

    private final transient GameService gameService = new GameService();
    private final transient ScoreDAO scoreDAO = new ScoreDAO();

    // ------------------------------------------------------------------
    //  GET
    // ------------------------------------------------------------------

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch (req.getServletPath()) {
            case "/game":
                showGame(req, resp);
                break;
            case "/result":
                showResult(req, resp);
                break;
            case "/leaderboard":
                showLeaderboard(req, resp);
                break;
            case "/lab":
                showLab(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    private void showGame(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        GameState game = currentGame(req);
        if (game == null || game.isFinished()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.setAttribute("game", game);
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    private void showResult(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        GameState game = currentGame(req);
        if (game == null || !game.isFinished()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.setAttribute("game", game);
        req.setAttribute("maxScore", MathService.MAX_SCORE);
        req.getRequestDispatcher("/result.jsp").forward(req, resp);
    }

    private void showLeaderboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<GameScore> scores = scoreDAO.findTopScores(LEADERBOARD_SIZE);
            req.setAttribute("scores", scores);
        } catch (SQLException e) {
            log("Could not read the leaderboard", e);
            req.setAttribute("error", "Could not read the leaderboard from the database. "
                    + "Is MySQL running and math_whiz_db created? (" + e.getMessage() + ")");
        }
        req.getRequestDispatcher("/leaderboard.jsp").forward(req, resp);
    }

    /** Math Lab: calls the real Java methods with numbers typed by the user. */
    private void showLab(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String type = req.getParameter("type");
        if (type != null) {
            req.setAttribute("labType", type);
            try {
                switch (type) {
                    case "prime": {
                        int n = Integer.parseInt(req.getParameter("n").trim());
                        int r = MathService.isPrime(n);
                        req.setAttribute("labResult", "isPrime(" + n + ") = " + r
                                + (r == 1 ? "  (prime)" : "  (not prime)"));
                        break;
                    }
                    case "mod": {
                        int a = Integer.parseInt(req.getParameter("a").trim());
                        int b = Integer.parseInt(req.getParameter("b").trim());
                        req.setAttribute("labResult", a + " % " + b + " = " + MathService.calculateMod(a, b));
                        break;
                    }
                    case "trig": {
                        String fn = req.getParameter("fn");
                        double deg = Double.parseDouble(req.getParameter("deg").trim().replace(',', '.'));
                        double r = MathService.calculateTrig(fn, deg);
                        req.setAttribute("labResult", fn + "(" + MathService.format(deg) + "\u00b0) = "
                                + MathService.format(r));
                        break;
                    }
                    default:
                        req.setAttribute("labError", "Unknown test type.");
                }
            } catch (NumberFormatException | NullPointerException e) {
                req.setAttribute("labError", "Validation error: please enter valid whole numbers "
                        + "(angles may be decimals).");
            } catch (IllegalArgumentException e) {
                req.setAttribute("labError", "Validation error: " + e.getMessage() + ".");
            } catch (ArithmeticException e) {
                req.setAttribute("labError", "Undefined: " + e.getMessage() + ".");
            }
        }
        req.getRequestDispatcher("/lab.jsp").forward(req, resp);
    }

    // ------------------------------------------------------------------
    //  POST
    // ------------------------------------------------------------------

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        if (!"/game".equals(req.getServletPath())) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        String action = req.getParameter("action");
        if ("start".equals(action)) {
            startGame(req, resp);
        } else if ("submit".equals(action)) {
            submitAnswers(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    /** Welcome page: Start Game. */
    private void startGame(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("player");
        name = (name == null) ? "" : name.trim();

        if (name.isEmpty()) {
            req.setAttribute("error", "Please enter your name to start the game.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }
        if (name.length() > MAX_NAME_LENGTH) {
            name = name.substring(0, MAX_NAME_LENGTH);
        }

        req.getSession().setAttribute("game", gameService.newGame(name));
        // Post/Redirect/Get: refreshing the question page never starts a second game
        resp.sendRedirect(req.getContextPath() + "/game");
    }

    /** Game page: Submit Answers. */
    private void submitAnswers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        GameState game = currentGame(req);
        if (game == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        if (game.isFinished()) {                       // already scored - never save twice
            resp.sendRedirect(req.getContextPath() + "/result");
            return;
        }

        String[] answers = new String[4];
        for (int i = 0; i < 4; i++) {
            answers[i] = req.getParameter("q" + (i + 1));
        }

        // 1. validate the input
        List<String> errors = GameService.validate(answers);
        if (!errors.isEmpty()) {
            redisplayGame(req, resp, game, answers, errors);
            return;
        }

        // 2. evaluate with the Java methods
        try {
            gameService.evaluate(game, answers);
        } catch (IllegalArgumentException | ArithmeticException e) {
            redisplayGame(req, resp, game, answers, List.of("Validation error: " + e.getMessage()));
            return;
        }

        // 3. save the score with JDBC
        GameScore score = new GameScore(game.getPlayer(), game.getTotalScore(), GameService.DIFFICULTY);
        try {
            scoreDAO.save(score);
        } catch (SQLException e) {
            log("Could not save the score", e);
            redisplayGame(req, resp, game, answers, List.of(
                    "Your score could not be saved. Is MySQL running and math_whiz_db created? ("
                    + e.getMessage() + ")"));
            return;
        }

        game.setPlayedAtText(score.getPlayedAtText());
        game.setFinished(true);
        resp.sendRedirect(req.getContextPath() + "/result");
    }

    private void redisplayGame(HttpServletRequest req, HttpServletResponse resp, GameState game,
                               String[] answers, List<String> errors) throws ServletException, IOException {
        req.setAttribute("game", game);
        req.setAttribute("errors", errors);
        req.setAttribute("entered", answers);
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    private static GameState currentGame(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (GameState) session.getAttribute("game");
    }
}
