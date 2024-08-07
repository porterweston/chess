package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class BoardRenderer {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private enum Color {WHITE, BLACK};
    private static ChessPosition curPos;
    private static ChessGame chessGame;

    public static void render(ChessGame game) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        chessGame = game;

        out.print(EscapeSequences.ERASE_SCREEN);

        out.print("\n\n");

        renderWhiteBottom(out);
        out.print("\n\n");
        renderBlackBottom(out);

        out.print("\n\n");
    }

    private static void renderWhiteBottom(PrintStream out) {
        renderBoard(out, Color.WHITE);
    }

    private static void renderBlackBottom(PrintStream out) {
        renderBoard(out, Color.BLACK);
    }

    private static void renderRow(PrintStream out, Color color, Color boardColor) {
        for (int curRow = 1; curRow <= BOARD_SIZE_IN_SQUARES; curRow++) {
            if ((curRow % 2 == 0 && color == Color.WHITE) || (curRow % 2 == 1 && color == Color.BLACK)) {
                setBlack(out);
                renderPiece(out);
            }
            else {
                setWhite(out);
                renderPiece(out);
            }
            if (boardColor == Color.WHITE) {
                curPos.setCol(curPos.getColumn()+1);
            }
            else {
                curPos.setCol(curPos.getColumn()-1);
            }
        }
        if (boardColor == Color.WHITE) {
            curPos.setCol(1);
        }
        else {
            curPos.setCol(8);
        }
        out.print(EscapeSequences.RESET_BG_COLOR);
    }

    private static void renderBoard(PrintStream out, Color color) {
        if (color == Color.WHITE) {
            curPos = new ChessPosition(8, 1);
        }
        else {
            curPos = new ChessPosition(1, 8);
        }
        renderHeader(out, color);
        out.print(EscapeSequences.RESET_BG_COLOR);
        for (int curCol = BOARD_SIZE_IN_SQUARES; curCol >= 1; curCol--) {
            if ((curCol % 2 == 0)) {
                renderRow(out, Color.WHITE, color);
            }
            else {
                renderRow(out, Color.BLACK, color);
            }
            setTextWhite(out);
            if (color == Color.WHITE) {
                out.printf(" %s ", curCol);
                curPos.setRow(curPos.getRow()-1);
                out.print(EscapeSequences.RESET_BG_COLOR);
            }
            else {
                out.printf(" %s ", 9-curCol);
                curPos.setRow(curPos.getRow()+1);
                out.print(EscapeSequences.RESET_BG_COLOR);
            }

            if (curCol > 1) {
                out.print("\n");
            }
        }
        out.print(EscapeSequences.RESET_BG_COLOR);
    }

    private static void renderHeader(PrintStream out, Color color) {
        setTextWhite(out);
        if (color == Color.WHITE) {
            out.printf(" a%sb%sc%sd%se%sf%sg%sh ", EscapeSequences.N_EMPTY, EscapeSequences.N_EMPTY,
                    EscapeSequences.N_EMPTY, EscapeSequences.N_EMPTY,
                    EscapeSequences.N_EMPTY, EscapeSequences.N_EMPTY,
                    EscapeSequences.N_EMPTY);
        }
        else {
            out.printf(" h%sg%sf%se%sd%sc%sb%sa ", EscapeSequences.N_EMPTY, EscapeSequences.N_EMPTY,
                    EscapeSequences.N_EMPTY, EscapeSequences.N_EMPTY,
                    EscapeSequences.N_EMPTY, EscapeSequences.N_EMPTY,
                    EscapeSequences.N_EMPTY);
        }
        out.print(EscapeSequences.RESET_BG_COLOR);
        out.print("\n");
    }

    private static void renderPiece(PrintStream out) {
        ChessPiece piece = chessGame.getBoard().getPiece(curPos);
        if (!(piece == null)) {
            ChessPiece.PieceType type = piece.getPieceType();
            ChessGame.TeamColor color = piece.getTeamColor();
            if (color == ChessGame.TeamColor.WHITE) {
                out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                switch (type) {
                    case PAWN -> out.print(EscapeSequences.WHITE_PAWN);
                    case KNIGHT -> out.print(EscapeSequences.WHITE_KNIGHT);
                    case BISHOP -> out.print(EscapeSequences.WHITE_BISHOP);
                    case ROOK -> out.print(EscapeSequences.WHITE_ROOK);
                    case QUEEN -> out.print(EscapeSequences.WHITE_QUEEN);
                    case KING -> out.print(EscapeSequences.WHITE_KING);
                }
            }
            else {
                out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                switch (type) {
                    case PAWN -> out.print(EscapeSequences.BLACK_PAWN);
                    case KNIGHT -> out.print(EscapeSequences.BLACK_KNIGHT);
                    case BISHOP -> out.print(EscapeSequences.BLACK_BISHOP);
                    case ROOK -> out.print(EscapeSequences.BLACK_ROOK);
                    case QUEEN -> out.print(EscapeSequences.BLACK_QUEEN);
                    case KING -> out.print(EscapeSequences.BLACK_KING);
                }
            }
        }
        else {
            out.print(EscapeSequences.EMPTY);
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
        out.print(EscapeSequences.SET_BG_COLOR_BROWN);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }

    private static void setTextWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_BOLD);
    }
}
