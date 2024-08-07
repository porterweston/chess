package ui;

import facade.*;

import java.util.Arrays;
import java.util.Locale;

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
        return String.format("help text");
    }

    private String quit() {
        return String.format("quit");
    }

    private String login(String[] params) throws ResponseException{
        return String.format("login text");
    }

    private String register(String[] params) throws ResponseException{
        return String.format("register text");
    }
}
