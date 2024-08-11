package ui;

import facade.*;

public abstract class GameUI extends UI{
    public abstract String help();

    public String leave() throws ResponseException {
        checkConnection();

        ws.leaveGame(authToken, currentGameID);
        ws = null;

        Repl.state = State.LOGGED_IN;
        return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE, "Leaving game...");
    }

    public String redraw() throws ResponseException {
        checkConnection();
        BoardRenderer.render(UI.getGame(UI.currentGameID), team);

        return "";
    }
}
