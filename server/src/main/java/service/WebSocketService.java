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
                notificationMessage = new NotificationMessage(String.format("%s has joined the game as white", username));
            }
            else if (username.equals(blackUser)) {
                notificationMessage = new NotificationMessage(String.format("%s has joined the game as black", username));
            }
            else {
                notificationMessage = new NotificationMessage(String.format("%s has joined the game as an observer", username));
            }

            return new ServerMessage[]{loadMessage, notificationMessage};
        } catch (DataAccessException e) {
            return new ServerMessage[]{new ErrorMessage("Unable to connect user")};
        }

    }

    public ServerMessage[] makeMove(String authToken, Integer gameID, ChessMove move){
        try {
            ChessGame game = gameDAO.getGame(gameID).game();
            //verify move is a valid move
            Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());
            if (!validMoves.contains(move)) {
                //invalid move
                return new ServerMessage[]{new ErrorMessage("Invalid move")};
            }
            //valid move, update game

        } catch (DataAccessException e) {
            return new ServerMessage[]{new ErrorMessage("Unable to make move")};
        }
    }

    public ServerMessage leaveGame(String authToken, Integer gameID){
        return new NotificationMessage("message");
    }

    public ServerMessage resignGame(String authToken, Integer gameID){
        return null;
    }
}
