package ui;

import chess.ChessGame;
import facade.*;
import model.GameData;
import reqres.*;

import java.util.Arrays;

public class PostLoginUI extends UI{
    public PostLoginUI() {
        super();
    }

    @Override
    public String eval(String line) {
        initializeGameIDs();
        var tokens = line.toLowerCase().split(" ");
        var cmd = tokens[0];
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "help" -> help();
                case "quit" -> quit();
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (ResponseException e) {
            return handleError(e.errorCode);
        }
    }

    private String help() {
        return String.format("%s%s%n%s%n%s%n%s%n%s%n%s%n%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE,
                "create <NAME> - a game",
                "list - games",
                "join <ID> [WHITE|BLACK] - a game",
                "observe <ID> - a game",
                "logout - when you are done",
                "quit - the application",
                "help - with available commands");
    }

    private String createGame(String[] params) throws ResponseException{
        checkConnection();
        if (params.length == 0) {
            throw new ResponseException(400, "bad request");
        }
        //build game name
        StringBuilder str = new StringBuilder();
        for (String s : params) {
            str.append(String.format("%s ", s));
        }
        str.deleteCharAt(str.length()-1);
        String gameName = str.toString();
        try {
            var result = facade.createGame(new CreateGameRequest(authToken, gameName));
            gameIDs.put(gameIDs.size()+1, result.gameID());
            return String.format("%s%s \"%s\"%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Created game", gameName);
        } catch (ResponseException e) {
            throw new ResponseException(Integer.parseInt(e.getMessage().substring(23)), "");
        }
    }

    private String listGames(String[] params) throws ResponseException{
        checkConnection();
        try {
            var result = facade.listGames(new ListGamesRequest(authToken));
            if (result.games().isEmpty()) {
                return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "No games to list");
            }
            StringBuilder str = new StringBuilder();
            int i=1;
            str.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            for (GameData game : result.games()) {
                String whitePlayer = game.whiteUsername();
                String blackPlayer = game.blackUsername();
                if (whitePlayer == null) {
                    whitePlayer = "None";
                }
                if (blackPlayer == null) {
                    blackPlayer = "None";
                }
                str.append(String.format("%s%s: \"%s%s%s\"%n%s   White: %s%s%s%n   Black: %s%s%n%s",
                        EscapeSequences.SET_TEXT_BOLD, i, EscapeSequences.SET_TEXT_COLOR_WHITE, game.gameName(),
                    EscapeSequences.SET_TEXT_COLOR_BLUE, EscapeSequences.RESET_TEXT_BOLD_FAINT,
                        EscapeSequences.SET_TEXT_COLOR_WHITE, whitePlayer, EscapeSequences.SET_TEXT_COLOR_BLUE,
                        EscapeSequences.SET_TEXT_COLOR_WHITE, blackPlayer, EscapeSequences.SET_TEXT_COLOR_BLUE));
                i++;
            }
            return str.toString();
        } catch (ResponseException e) {
            throw new ResponseException(Integer.parseInt(e.getMessage().substring(23)), "");
        }
    }

    private String joinGame(String[] params) throws ResponseException{
        checkConnection();
        if (gameIDs.isEmpty()) {
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "No games to join");
        }
        if (params.length == 0 || isBadJoinObserveInput(params)) {
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Please input a number");
        }
        if (params.length != 2) {
            throw new ResponseException(400, "bad request");
        }
        try {
            ChessGame.TeamColor playerColor = null;
            if (params[1].equals("white")) {
                playerColor = ChessGame.TeamColor.WHITE;
            }
            if (params[1].equals("black")) {
                playerColor = ChessGame.TeamColor.BLACK;
            }

            team = playerColor;
            int gameID = gameIDs.get(Integer.parseInt(params[0]));
            facade.joinGame(new JoinGameRequest(authToken, playerColor, gameID));
            Repl.state = State.IN_GAME;
            currentGameID = gameID;

            //websocket
            ws = new WebSocketFacade(8080, gameHandler);
            ws.connect(authToken, currentGameID);

            return "";
        }
        catch (NullPointerException e) {
            throw new ResponseException(400, "bad request");
        }
        catch (ResponseException e) {
            throw new ResponseException(Integer.parseInt(e.getMessage().substring(23)), "");
        }
    }

    private String observeGame(String[] params) throws ResponseException{
        checkConnection();
        if (gameIDs.isEmpty()) {
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "No games to observe");
        }
        if (params.length == 0 || isBadJoinObserveInput(params)) {
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Please input a number");
        }
        if (params.length != 1) {
            throw new ResponseException(400, "bad request");
        }
        try {
            currentGameID = gameIDs.get(Integer.parseInt(params[0]));
        } catch (Exception e) {
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Game doesn't exist");
        }

        Repl.state = State.OBSERVING_GAME;
        team = ChessGame.TeamColor.WHITE;

        //websocket
        ws = new WebSocketFacade(8080, gameHandler);
        ws.connect(authToken, currentGameID);

        return "";
    }

    private String logout() throws ResponseException{
        checkConnection();
        try {
            facade.logout(new LogoutRequest(authToken));
            Repl.state = State.LOGGED_OUT;
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Logged out");
        } catch (ResponseException e) {
            throw new ResponseException(Integer.parseInt(e.getMessage().substring(23)), "");
        }
    }

    private void initializeGameIDs() {
        gameIDs.clear();
        try {
            var listGamesResult = facade.listGames(new ListGamesRequest(authToken));
            int i=1;
            for (GameData game : listGamesResult.games()) {
                gameIDs.put(i, game.gameID());
                i++;
            }

        } catch (ResponseException e) {
            System.out.printf("%s: %s", "Unable to initialize gameIDs", e.getMessage());
        }
    }

    //returns if a given set of parameters has bad input for join or observe commands
    private boolean isBadJoinObserveInput(String[] params) {
        try {
            gameIDs.get(Integer.parseInt(params[0]));
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}