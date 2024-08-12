package ui;

import chess.ChessGame;
import chess.ChessPosition;
import facade.*;
import reqres.LogoutRequest;

public abstract class GameUI extends UI implements GameHandler{
    public GameUI() {
        super();
        gameHandler = this;
    }

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

    @Override
    public String quit() throws ResponseException {
        leave();
        return super.quit();
    }

    public String highlight(String[] params) throws ResponseException {
        checkConnection();

        //ensure good input
        if (params.length != 1) {
            throw new ResponseException(400, "bad request");
        }
        if (!params[0].matches("^[a-h][1-8]$")) {
            throw new ResponseException(400, "bad request");
        }

        int row = Integer.parseInt(String.valueOf(params[0].charAt(1)));
        int col = readCol(params[0].charAt(0));
        ChessPosition position = new ChessPosition(row, col);

        BoardRenderer.renderValidMoves(UI.getGame(UI.currentGameID), team, position);

        return "";
    }

    public int readCol(char c) {
        int col = 0;
        switch(c) {
            case 'a' -> col = 1;
            case 'b' -> col = 2;
            case 'c' -> col = 3;
            case 'd' -> col = 4;
            case 'e' -> col = 5;
            case 'f' -> col = 6;
            case 'g' -> col = 7;
            case 'h' -> col = 8;
        }
        return col;
    }

    public void updateGame(ChessGame game){
        try {
            redraw();
            Repl.printPrompt();
        } catch (ResponseException e) {
            handleError(e.errorCode);
        }
    }

    public void printMessage(String message) {
        System.out.printf("%s%s%n", EscapeSequences.SET_TEXT_COLOR_YELLOW, message);
        Repl.printPrompt();
    }
}
