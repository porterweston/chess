package service;

import chess.*;
import dataaccess.mysql.MySQLAuthDAO;
import server.Server;
import websocket.messages.*;
import dataaccess.*;
import dataaccess.mysql.*;
import model.*;

import java.util.Collection;

public class WebSocketService {
    private static MySQLGameDAO gameDAO;
    private static MySQLAuthDAO authDAO;

    public WebSocketService() {
        try {
            gameDAO = new MySQLGameDAO();
            authDAO = new MySQLAuthDAO();
        } catch (DataAccessException e) {
            System.out.printf("Unable to initialize database: %s", e.getMessage());
        }
    }

    public ServerMessage[] connect(String authToken, Integer gameID) {
        try {
            GameData gameData = gameDAO.getGame(gameID);
            LoadGameMessage loadMessage = new LoadGameMessage(gameData.game());

            String username = authDAO.getAuth(authToken).username();
            //access the color that the user joined as
            String whiteUser = gameData.whiteUsername();
            String blackUser = gameData.blackUsername();
            NotificationMessage notificationMessage;
            if (username.equals(whiteUser)) {
                notificationMessage = new NotificationMessage(
                        String.format("%s has joined the game as white", username));
            }
            else if (username.equals(blackUser)) {
                notificationMessage = new NotificationMessage(
                        String.format("%s has joined the game as black", username));
            }
            else {
                notificationMessage = new NotificationMessage(
                        String.format("%s has joined the game as an observer", username));
            }

            return new ServerMessage[]{loadMessage, notificationMessage};
        } catch (DataAccessException e) {
            return new ServerMessage[]{new ErrorMessage("Unable to connect user")};
        }

    }

    public ServerMessage[] makeMove(String authToken, Integer gameID, ChessMove move){
        try {
            GameData gameData = gameDAO.getGame(gameID);
            ChessGame game = gameData.game();
            String username = authDAO.getAuth(authToken).username();
            String opponentUsername = getOpponentUsername(username, gameData);

            //check if game is over
            if (game.getGameOverStatus()) {
                return new ServerMessage[]{new ErrorMessage("Game is over")};
            }

            //check if user is trying to move other team's piece
            ChessGame.TeamColor pieceColor = null;
            if (game.getBoard().getPiece(move.getStartPosition()) != null) {
                pieceColor = game.getBoard().getPiece(move.getStartPosition()).getTeamColor();
            }
            if ((username.equals(gameData.whiteUsername()) && pieceColor == ChessGame.TeamColor.BLACK) ||
                    (username.equals(gameData.blackUsername()) && pieceColor == ChessGame.TeamColor.WHITE)) {
                return new ServerMessage[]{new ErrorMessage("Can't move other player's piece")};
            }

            //check if observer is trying to make move
            if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
                return new ServerMessage[]{new ErrorMessage("Can't make a move as an observer")};
            }

            //make the move
            game.makeMove(move);
            LoadGameMessage loadMessage = new LoadGameMessage(game);
            NotificationMessage notificationMessage = new NotificationMessage(
                    String.format("%s moved from %s to %s", username,
                            move.getStartPosition().toString(), move.getEndPosition().toString()));

            //if in check-checkmate-stalemate
            ChessGame.TeamColor team = game.getTeamTurn();
            NotificationMessage statusMessage = null;
            if (game.isInCheck(team) && !game.isInCheckmate(team)) {
                statusMessage = new NotificationMessage(String.format("%s is in check", opponentUsername));
            }
            else if (game.isInCheckmate(team)) {
                statusMessage = new NotificationMessage(
                        String.format("%s is in checkmate%n%s has won the game!", opponentUsername, username));
                game.setGameOver();
            }
            else if (game.isInStalemate(team)) {
                statusMessage = new NotificationMessage("Game is in stalemate%nGame is over");
                game.setGameOver();
            }

            //update game in database
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), game);
            gameDAO.updateGame(gameData, newGameData);

            return new ServerMessage[]{loadMessage, notificationMessage, statusMessage};
        } catch (DataAccessException e) {
            return new ServerMessage[]{new ErrorMessage("Unable to make move")};
        } catch (InvalidMoveException e) {
            return new ServerMessage[]{new ErrorMessage(e.getMessage())};
        }
    }

    public ServerMessage leaveGame(String authToken, Integer gameID){
        try {
            String username = authDAO.getAuth(authToken).username();
            GameData gameData = gameDAO.getGame(gameID);
            GameData newGameData = null;
            if (username.equals(gameData.whiteUsername())) {
                newGameData = new GameData(gameData.gameID(), null,
                        gameData.blackUsername(), gameData.gameName(), gameData.game());
            }
            else if (username.equals(gameData.blackUsername())) {
                newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(),
                        null, gameData.gameName(), gameData.game());
            }
            else {
                newGameData = gameData;
            }
            gameDAO.updateGame(gameData, newGameData);
            return new NotificationMessage(String.format("%s has left the game", username));
        } catch (DataAccessException e) {
            return new ErrorMessage("Unable to leave game");
        }
    }

    public ServerMessage resignGame(String authToken, Integer gameID){
        try {
            GameData gameData = gameDAO.getGame(gameID);
            ChessGame game = gameData.game();
            String username = authDAO.getAuth(authToken).username();
            String opponentUsername = getOpponentUsername(username, gameData);

            //check if game is already over
            if (game.getGameOverStatus()) {
                return new ErrorMessage("Game is over");
            }

            //check if user is an observer or game is already over
            if ((!(gameData.whiteUsername().equals(username)) && (!(gameData.blackUsername().equals(username))))) {
                return new ErrorMessage("Unable to resign game");
            }

            game.setGameOver();

            //update game in database
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), game);
            gameDAO.updateGame(gameData, newGameData);

            return new NotificationMessage(String.format("%s has resigned%n%s has won the game!", username, opponentUsername));
        } catch (DataAccessException e) {
            return new ErrorMessage("Unable to resign game");
        }
    }

    private String getOpponentUsername(String username, GameData gameData) {
        //get opponent's username
        String opponentUsername = null;
        if (username.equals(gameData.whiteUsername())) {
            opponentUsername = gameData.blackUsername();
        }
        else if (username.equals(gameData.blackUsername())) {
            opponentUsername = gameData.whiteUsername();
        }
        return opponentUsername;
    }
}
