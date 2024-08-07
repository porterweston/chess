package ui;

import facade.ResponseException;
import reqres.LogoutRequest;

import java.util.Arrays;

public abstract class GameUI extends UI{
    @Override
    public String eval(String line) {
        var tokens = line.toLowerCase().split(" ");
        var cmd = tokens[0];
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "help" -> help();
                case "quit" -> quit();
                case "leave" -> leave();
                default -> help();
            };
        } catch (ResponseException e) {
            return handleError(e.errorCode);
        }
    }

    public abstract String help();

    public String leave() {
        Repl.state = State.LOGGED_IN;
        return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Leaving game...");
    }
}
