package dataaccess.mysql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import dataaccess.interfaces.*;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class MySQLGameDAO extends MySQLDAO implements GameDAO{
    public MySQLGameDAO() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
                gameID INT NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(255) DEFAULT NULL,
                blackUsername VARCHAR(255) DEFAULT NULL,
                gameName VARCHAR(255) NOT NULL,
                game TEXT DEFAULT NULL,
                PRIMARY KEY (gameID)
            );
            """
        };
        configureDatabase(createStatements);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        if (gameID == 0) {
            throw new DataAccessException("Game does not exist");
        }
        try (var conn = DatabaseManager.getConnection()) {
            String preparedStatement = "SELECT * FROM games WHERE gameID=?;";
            try (var ps = conn.prepareStatement(preparedStatement)) {
                ps.setInt(1, gameID);
                ResultSet rs = ps.executeQuery();
                rs.next();
                //deserialize game json
                ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                        rs.getString("blackUsername"), rs.getString("gameName"), game);
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Game does not exist");
        }
    }

    public int createGame(String gameName) {
        if (gameName == null) {
            return 0;
        }
        String preparedStatement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        var game = new ChessGame();
        var gameJson = new Gson().toJson(game);
        try {
            return executeUpdate(preparedStatement, gameName, gameJson);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    public Collection<GameData> listGames() {
        Collection<GameData> games = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            String preparedStatement = "SELECT * FROM games";
            try (var ps = conn.prepareStatement(preparedStatement)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    games.add(new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                            rs.getString("blackUsername"), rs.getString("gameName"), game));
                }
                return games;
            }
        } catch (DataAccessException | SQLException e) {
            return null;
        }
    }

    public void updateGame(GameData gameData, GameData newData) throws DataAccessException {
        //delete old game data
        String preparedStatement = "DELETE FROM games WHERE gameID=?;";
        executeUpdate(preparedStatement, gameData.gameID());
        //add new game data
        String game = new Gson().toJson(newData.game());
        preparedStatement = """
                            INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) 
                            VALUES (?, ?, ?, ?, ?)
                            """;
        executeUpdate(preparedStatement, newData.gameID(), newData.whiteUsername(), newData.blackUsername(),
                newData.gameName(), game);
    }

    public void deleteGames() {
        String preparedStatement = "TRUNCATE games;";
        try {
            executeUpdate(preparedStatement);
        } catch (DataAccessException e) {
            return;
        }
    }

    public Collection<GameData> getGamesDatabase() {
        HashSet<GameData> games = new HashSet<>();
        try (var conn = DatabaseManager.getConnection()) {
            String preparedStatement = "SELECT * FROM games";
            try (var ps = conn.prepareStatement(preparedStatement)) {
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    GameData curGame = new GameData(rs.getInt("gameID"),
                            rs.getString("whiteUsername"), rs.getString("blackUsername"),
                            rs.getString("gameName"), game);
                    games.add(curGame);
                }
            }
            return games;
        } catch (DataAccessException | SQLException e) {
            return null;
        }
    }
}
