package dataaccess;

import model.*;
import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

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
    public int createGame(String gameName) {
        Random r = new Random();
        int gameID = r.nextInt(1, Integer.MAX_VALUE);
        //ensure a game with that ID doesn't exist
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                //if it does, try generating a game again
                return createGame(gameName);
            }
        }
        games.add(new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
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

    //returns what's in the database for testing purposes
    public Collection<GameData> getGamesDatabase() {
        return games;
    }
}
