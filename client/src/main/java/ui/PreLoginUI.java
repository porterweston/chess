package ui;

import facade.*;
import reqres.*;

import java.util.Arrays;

public class PreLoginUI extends UI{
    public PreLoginUI() {
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
                case "login" -> login(params);
                case "register" -> register(params);
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    private String help() {
        return String.format("%s%s%n%s%n%s%n%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE,
                "register <USERNAME> <PASSWORD> <EMAIL> - a user",
                "login <USERNAME> <PASSWORD> - to play chess",
                "quit - the application",
                "help - with available commands");
    }

    private String quit() {
        return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Quitting client...");
    }

    private String login(String[] params) throws ResponseException{
        if (params.length != 2) {
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, handleError("400"));
        }
        try {
            LoginResult result = facade.login(new LoginRequest(params[0], params[1]));
            Repl.state = State.LOGGED_IN;
            authToken = result.authToken();
            return String.format("%s%s %s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Logged in", result.username());
        } catch (ResponseException e) {
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, handleError(e.getMessage().substring(23)));
        }
    }

    private String register(String[] params) throws ResponseException{
        if (params.length != 3) {
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, handleError("400"));
        }
        try {
            RegisterResult result = facade.register(new RegisterRequest(params[0], params[1], params[2]));
            Repl.state = State.LOGGED_IN;
            authToken = result.authToken();
            return String.format("%s%s %s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Registered", result.username());
        } catch (ResponseException e) {
            return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, handleError(e.getMessage().substring(23)));
        }
    }
}
