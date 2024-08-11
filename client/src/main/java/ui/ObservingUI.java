package ui;

import facade.ResponseException;
import reqres.LogoutRequest;

import java.util.Arrays;

public class ObservingUI extends GameUI {
    public ObservingUI() {
        super();
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
