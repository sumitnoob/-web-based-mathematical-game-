-- =====================================================================
--  SUMIT - MATH WHIZ  |  Database creation script (MySQL 8+)
--  Run it once:   mysql -u root -p < database/math_whiz_db.sql
--  (or paste it into MySQL Workbench and press the lightning button)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS math_whiz_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE math_whiz_db;

CREATE TABLE IF NOT EXISTS game_scores (
    id          INT          NOT NULL AUTO_INCREMENT,
    player_name VARCHAR(100) NOT NULL,
    score       INT          NOT NULL,
    difficulty  VARCHAR(20)  NOT NULL DEFAULT 'NORMAL',
    played_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- ---------------------------------------------------------------------
--  Queries used by the application (for reference)
-- ---------------------------------------------------------------------

-- Save one finished game (the app uses a PreparedStatement with ? marks)
-- INSERT INTO game_scores (player_name, score, difficulty, played_at)
-- VALUES ('Sumit', 10, 'NORMAL', NOW());

-- Show every saved record
-- SELECT * FROM game_scores;

-- Top 10 leaderboard: highest score first, earlier date/time wins a tie
-- SELECT * FROM game_scores ORDER BY score DESC, played_at ASC LIMIT 10;
