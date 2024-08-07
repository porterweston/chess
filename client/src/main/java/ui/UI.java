package ui;

import facade.*;
import reqres.*;
import model.*;

import java.util.HashMap;

public abstract class UI {
    public static ServerFacade facade;
    public static String authToken;
    public static HashMap<Integer, Integer> games;
    public static int currentGame;

    public UI() {
        facade = new ServerFacade(8080);
        authToken = null;
        games = new HashMap<Integer, Integer>();
        currentGame = 0;
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
}
