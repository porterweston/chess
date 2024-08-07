package ui;

import facade.*;
import reqres.*;
import model.*;
import chess.*;

import java.util.HashMap;

public abstract class UI {
    public static ServerFacade facade;
    public static String authToken;
    //map where the key is the client's gameID and the value is the server's gameID
    public static HashMap<Integer, Integer> gameIDs;
    public static int currentGameID;

    public UI() {
        facade = new ServerFacade(8080);
        authToken = null;
        gameIDs = new HashMap<Integer, Integer>();
        currentGameID = 0;
    }

    public abstract String eval(String line);

    public String handleError(int errorCode) {
        StringBuilder str = new StringBuilder();
        str.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
        switch(errorCode) {
            case 401 -> str.append(String.format("%s%n", "Error: unauthorized"));
            case 403 -> str.append(String.format("%s%n", "Error: already taken"));
            case 500 -> str.append(String.format("%s%n", "Error: not connected to server"));
            case 501 -> str.append(String.format("%s%n", "Error: please input a number"));
            default -> str.append(String.format("%s%n", "Error: bad request"));
        }
        return str.toString();
    }

    public void checkConnection() throws ResponseException{
        if (!facade.isConnected()) {
            throw new ResponseException(500, "not connected");
        }
    }

    public static ChessGame getGame(int gameID) {
        try {
            var result = facade.listGames(new ListGamesRequest(authToken));
            for (GameData game : result.games()) {
                if (game.gameID() == gameID) {
                    return game.game();
                }
            }
        } catch (ResponseException e) {
            System.out.println("unable to get game");
        }
        return null;
    }
}
