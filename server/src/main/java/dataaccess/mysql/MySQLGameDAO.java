package dataaccess.mysql;

import dataaccess.*;
import dataaccess.interfaces.*;
import model.*;

import javax.xml.crypto.Data;
import java.util.Collection;

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
        return null;
    }

    public int createGame(String gameName) {
        return 0;
    }

    public Collection<GameData> listGames() {
        return null;
    }

    public void updateGame(GameData gameData, GameData newData) throws DataAccessException {

    }

    public void deleteGames() {

    }
}
