package service;

import chess.*;
import dataaccess.mysql.MySQLAuthDAO;
import websocket.messages.*;
import dataaccess.*;
import dataaccess.mysql.*;
import model.*;

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

    public ServerMessage[] connect(String authToken, Integer gameID) throws ErrorException{
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
            throw new ErrorException(400, e.getMessage());
        }

    }

    public ServerMessage[] makeMove(String authToken, Integer gameID, ChessMove move) throws ErrorException{
        return null;
    }

    public ServerMessage leaveGame(String authToken, Integer gameID) throws ErrorException{
        return new NotificationMessage("message");
    }

    public ServerMessage resignGame(String authToken, Integer gameID) throws ErrorException{
        return null;
    }
}
