package dataaccess.mysql;

import dataaccess.*;
import dataaccess.interfaces.*;
import model.*;

import java.util.Collection;

public class MySQLGameDAO implements GameDAO{
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
