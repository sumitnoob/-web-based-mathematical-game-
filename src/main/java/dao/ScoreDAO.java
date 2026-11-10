package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.GameScore;

/** TASK 07 and 08 - all JDBC code for the game_scores table lives here. */
public class ScoreDAO {

    private static final String INSERT_SQL =
            "INSERT INTO game_scores (player_name, score, difficulty, played_at) VALUES (?, ?, ?, ?)";

    private static final String ALL_SQL =
            "SELECT id, player_name, score, difficulty, played_at FROM game_scores ORDER BY id";

    private static final String TOP_SQL =
            "SELECT id, player_name, score, difficulty, played_at FROM game_scores "
          + "ORDER BY score DESC, played_at ASC, id ASC LIMIT ?";

    /**
     * Saves one finished game (name, score, difficulty and the current date/time)
     * using a PreparedStatement. Fills in the id and the played time of {@code gameScore}.
     */
    public void save(GameScore gameScore) throws SQLException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, gameScore.getPlayerName());
            ps.setInt(2, gameScore.getScore());
            ps.setString(3, gameScore.getDifficulty());
            ps.setTimestamp(4, now);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    gameScore.setId(keys.getInt(1));
                }
            }
        }
        gameScore.setPlayedAt(now);
    }

    /** Retrieves every saved record (oldest first). */
    public List<GameScore> findAll() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            return readAll(rs);
        }
    }

    /**
     * The leaderboard: highest score first; when scores are equal the earlier
     * date/time ranks higher.
     */
    public List<GameScore> findTopScores(int limit) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(TOP_SQL)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                return readAll(rs);
            }
        }
    }

    private static List<GameScore> readAll(ResultSet rs) throws SQLException {
        List<GameScore> list = new ArrayList<>();
        while (rs.next()) {
            GameScore s = new GameScore();
            s.setId(rs.getInt("id"));
            s.setPlayerName(rs.getString("player_name"));
            s.setScore(rs.getInt("score"));
            s.setDifficulty(rs.getString("difficulty"));
            s.setPlayedAt(rs.getTimestamp("played_at"));
            list.add(s);
        }
        return list;
    }
}
