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
        return String.format("%s%n", "observingUI help text");
    }
}
