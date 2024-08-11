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
            ChessGame game = gameDAO.getGame(gameID).game();
            String username = authDAO.getAuth(authToken).username();
            game.makeMove(move);
            LoadGameMessage loadMessage = new LoadGameMessage(game);
            NotificationMessage notificationMessage = new NotificationMessage(
                    String.format("%s moved %s from %s to %s", username,
                            game.getBoard().getPiece(move.getStartPosition()).getPieceType().toString(),
                            move.getStartPosition().toString(), move.getEndPosition().toString()));
            //if in check/checkmate/stalemate
            ChessGame.TeamColor team = game.getTeamTurn();
            NotificationMessage statusMessage = null;
            if (game.isInCheck(team)) {
                statusMessage = new NotificationMessage(String.format("%s is in check", username));
            }
            else if (game.isInCheckmate(team)) {
                statusMessage = new NotificationMessage(
                        String.format("%s is in checkmate", username));
            }
            else if (game.isInStalemate(team)) {
                statusMessage = new NotificationMessage("Game is in stalemate");
            }
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
            game.setGameOver();
            //check if user is an observer or game is already over
            if (!(gameData.whiteUsername().equals(username)) || (!(gameData.blackUsername().equals(username))) ||
                game.getGameOverStatus()) {
                return new ErrorMessage("Unable to resign game");
            }
            return new NotificationMessage(String.format("%s has resigned", username));
        } catch (DataAccessException e) {
            return new ErrorMessage("Unable to resign game");
        }
    }
}
