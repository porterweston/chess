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
        return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "gameplayUI help text");
    }
}
