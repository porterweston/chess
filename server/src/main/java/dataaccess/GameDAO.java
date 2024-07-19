package dataaccess;

import model.*;

import java.util.Collection;

public interface GameDAO {
    public GameData getGame(int gameID) throws DataAccessException;
    public void createGame(GameData gameData) throws DataAccessException;
    public Collection<GameData> listGames();
    public void updateGame(GameData gameData, GameData newData) throws DataAccessException;
    public void deleteGames();
}
