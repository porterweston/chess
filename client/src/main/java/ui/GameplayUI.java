package ui;

import chess.*;
import facade.*;

import java.util.Arrays;

public class GameplayUI extends GameUI implements GameHandler{
    public GameplayUI() {
        super();
        gameHandler = this;
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
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (ResponseException e) {
            return handleError(e.errorCode);
        }
    }

    @Override
    public String help() {
        return String.format("%s%s%n", EscapeSequences.SET_TEXT_COLOR_BLUE,
                String.format("%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s",
                        "redraw - the chess board",
                        "leave - the game",
                        "move <POSITION> <POSITION> [PROMOTION] - a chess piece",
                        "   ex: move a2 a3",
                        "       move a7 a8 queen",
                        "resign - the game",
                        "highlight <POSITION> - legal moves",
                        "   ex: highlight a2",
                        "quit - the application",
                        "help - with available commands"));

    }

    private String move(String[] params) throws ResponseException {
        checkConnection();

        if (params.length != 2 && params.length != 3) {
            throw new ResponseException(400, "bad request");
        }

        int row = Integer.parseInt(String.valueOf(params[0].charAt(1)));
        int col = readCol(params[0].charAt(0));
        ChessPosition startPos = new ChessPosition(row, col);

        row = Integer.parseInt(String.valueOf(params[1].charAt(1)));
        col = readCol(params[1].charAt(0));
        ChessPosition endPos = new ChessPosition(row, col);

        ChessPiece.PieceType type = null;
        if (params.length == 3) {
            type = ChessPiece.PieceType.valueOf(params[2]);
        }

        ChessMove move = new ChessMove(startPos, endPos, type);

        ws.makeMove(authToken, currentGameID, move);
        return "";
    }

    private String resign() throws ResponseException {
        checkConnection();

        ws.resignGame(authToken, currentGameID);

        return "";
    }

    private String highlight(String[] params) throws ResponseException {
        checkConnection();
        return null;
    }

    @Override
    public void updateGame(ChessGame game){
        try {
            redraw();
            Repl.printPrompt();
        } catch (ResponseException e) {
            handleError(e.errorCode);
        }
    }

    @Override
    public void printMessage(String message) {
        System.out.printf("%s%s%n", EscapeSequences.SET_TEXT_COLOR_YELLOW, message);
        Repl.printPrompt();
    }

    private int readCol(char c) {
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
}
