package ui;

import chess.*;

public interface GameHandler {
    public void updateGame(ChessGame game);
    public void printMessage(String message);
}
