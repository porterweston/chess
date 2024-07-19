package dataaccess;

import model.*;

import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{

    private static Collection<GameData> games = new HashSet<GameData>();

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Game does not exist");
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        if (games.contains(gameData)) {
            throw new DataAccessException("Game already exists");
        }
        else {
            games.add(gameData);
        }
    }

    @Override
    public Collection<GameData> listGames() {
        return games;
    }

    @Override
    public void updateGame(GameData gameData, GameData newData) throws DataAccessException {
        for (GameData game : games) {
            if (game.equals(gameData)) {
                games.remove(game);
                games.add(newData);
                return;
            }
        }
        throw new DataAccessException("Game does not exist");
    }

    @Override
    public void deleteGames() {
        games.clear();
    }
}
