package ui;

import chess.*;

public class BoardRenderer {
    public static void render(ChessGame game) {
        System.out.printf("%s%n%s%n", "This is a chess board!", game.toString());
    }
}
