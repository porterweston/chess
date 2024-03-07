package dataAccess;

import model.GameData;

public interface GameDAO {
    public GameData getGame(int gameID) throws DataAccessException;
    public GameData[] listGames() throws DataAccessException;
    public void createGame(String gameName) throws DataAccessException;
    public boolean joinGame(String clientColor, int gameID) throws DataAccessException;
    public void clearGames() throws DataAccessException;
}
