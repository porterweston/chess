package service;

import chess.ChessGame;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import model.*;
import dataaccess.*;
import reqres.*;

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
        try {
            //verify authorization
            AuthData auth = authDAO.getAuth(req.authToken());

            try {
                Integer gameID = req.gameID();
                //if any fields in request are null, bad request
                if (gameID == null || req.playerColor() == null || req.authToken() == null) {
                    throw new ErrorException(400, "bad request");
                }

                //get game
                GameData game = gameDAO.getGame(gameID);

                //verify user can join game
                if (req.playerColor() == ChessGame.TeamColor.WHITE && game.whiteUsername() != null ||
                    req.playerColor() == ChessGame.TeamColor.BLACK && game.blackUsername() != null) {
                    throw new ErrorException(403, "already taken");
                }

                //update game
                GameData updatedGame = null;
                if (req.playerColor() == ChessGame.TeamColor.WHITE) {
                    updatedGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
                }
                else if (req.playerColor() == ChessGame.TeamColor.BLACK) {
                    updatedGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
                }
                gameDAO.updateGame(game, updatedGame);

                //return result
                return new JoinGameResult();
            }
            catch (DataAccessException exception) {
                throw new ErrorException(500, "game doesn't exist");
            }

        }
        catch (DataAccessException exception) {
            throw new ErrorException(401, "unauthorized");
        }
    }
}
