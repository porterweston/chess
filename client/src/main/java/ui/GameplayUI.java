package ui;

import chess.ChessGame;
import facade.ResponseException;
import reqres.LogoutRequest;

import java.util.Arrays;

public class GameplayUI extends GameUI implements GameHandler{
    public GameplayUI() {
        super();
    }

    @Override
    public String eval(String line) {
        var tokens = line.toLowerCase().split(" ");
        var cmd = tokens[0];
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "help" -> help();
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException e) {
            return handleError(e.errorCode);
        }
    }

    @Override
    public String help() {
        return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE,
                String.format("%s%n%s%n%s%n%s%n%s%n%s%n%s",
                        "redraw - the chess board",
                        "leave - the game",
                        "move <POSITION> <POSITION> - a chess piece",
                        "resign - the game",
                        "highlight <POSITION> - legal moves",
                        "quit - the application",
                        "help - with available commands"));

    }

    private void redraw() throws ResponseException{
        checkConnection();
    }

    private void

    @Override
    public void updateGame(ChessGame game) {

    }

    @Override
    public void printMessage(String message) {
        System.out.printf("%s%s%n", EscapeSequences.SET_TEXT_COLOR_YELLOW, message);
    }
}
