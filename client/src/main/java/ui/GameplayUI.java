package ui;

import facade.ResponseException;
import reqres.LogoutRequest;

import java.util.Arrays;

public class GameplayUI extends UI{
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

    private String help() {
        return "gameplayUI help text";
    }

    private String quit() throws ResponseException {
        try {
            facade.logout(new LogoutRequest(authToken));
            return String.format("%s%s%n%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE,
                    "Logging out...", "Quitting application...");
        } catch (ResponseException e) {
            throw new ResponseException(e.errorCode, "");
        }
    }
}
