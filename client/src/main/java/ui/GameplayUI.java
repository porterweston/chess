package ui;

import facade.ResponseException;
import reqres.LogoutRequest;

import java.util.Arrays;

public class GameplayUI extends GameUI{
    public GameplayUI() {
        super();
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
}
