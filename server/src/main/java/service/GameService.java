package service;

import model.*;
import dataaccess.*;

public class GameService {

    private MemoryGameDAO gameDAO = new MemoryGameDAO();
    private MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public ListGamesResult listGames(ListGamesRequest req) throws ErrorException{
        try {
            //verify authorization
            AuthData auth = authDAO.getAuth(req.authToken());

            return new ListGamesResult(gameDAO.listGames());
        }
        catch (DataAccessException exception) {
            throw new ErrorException(401, "unauthorized");
        }
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ErrorException{
        try {
            //verify authorization
            AuthData auth = authDAO.getAuth(req.authToken());

            //create game
            int gameID = gameDAO.createGame(req.gameName());
            return new CreateGameResult(gameID);
        }
        catch (DataAccessException exception) {
            throw new ErrorException(401, "unauthorized");
        }
    }

    public JoinGameResult joinGame(JoinGameRequest req) throws ErrorException{
        return null;
    }
}
