package ui;

import chess.ChessGame;
import facade.ResponseException;
import org.glassfish.grizzly.http.server.Response;
import reqres.LogoutRequest;

import java.util.Arrays;

public class ObservingUI extends GameUI implements GameHandler {
    public ObservingUI() {
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
                case "redraw" -> redraw();
                case "leave" -> leave();
                default -> help();
            };
        } catch (ResponseException e) {
            return handleError(e.errorCode);
        }
    }

    @Override
    public String help() {
        return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE,
                String.format("%s%n%s%n%s%n%s",
                        "redraw - the chess board",
                        "leave - game",
                        "quit - the application",
                        "help - with available commands"));
    }
}
