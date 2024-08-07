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
        return String.format("%s%n", "gameplayUI help text");
    }
}
