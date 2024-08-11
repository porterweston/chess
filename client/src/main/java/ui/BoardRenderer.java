package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;

public class BoardRenderer {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private enum Color {WHITE, BLACK};
    private static ChessPosition curPos;
    private static ChessGame chessGame;
    private static Collection<ChessPosition> validPositions;
    private static ChessPosition pos;

    public static void render(ChessGame game, ChessGame.TeamColor team) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        chessGame = game;

        out.print(EscapeSequences.ERASE_SCREEN);

        out.print("\n\n");

        if (team == ChessGame.TeamColor.WHITE) {
            renderWhiteBottom(out, false);
        }
        else {
            renderBlackBottom(out, false);
        }

        out.print("\n\n");
    }

    public static void renderValidMoves(ChessGame game, ChessGame.TeamColor team, ChessPosition position) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        chessGame = game;

        out.print(EscapeSequences.ERASE_SCREEN);

        out.print("\n\n");

        if (game.getBoard().getPiece(position) != null) {
            pos = position;
            Collection<ChessMove> validMoves = game.validMoves(position);
            validPositions = new HashSet<>();
            validPositions.add(position);
            for (ChessMove move : validMoves) {
                validPositions.add(move.getEndPosition());
            }

            if (team == ChessGame.TeamColor.WHITE) {
                renderWhiteBottom(out, true);
            }
            else {
                renderBlackBottom(out, true);
            }
        }
        else {
            if (team == ChessGame.TeamColor.WHITE) {
                renderWhiteBottom(out, false);
            }
            else {
                renderBlackBottom(out, false);
            }
        }

        out.print("\n\n");
    }

    private static void renderWhiteBottom(PrintStream out, boolean highlight) {
        renderBoard(out, Color.WHITE, highlight);
    }

    private static void renderBlackBottom(PrintStream out, boolean highlight) {
        renderBoard(out, Color.BLACK, highlight);
    }

    private static void renderRow(PrintStream out, Color color, Color boardColor, boolean highlight) {
        for (int curRow = 1; curRow <= BOARD_SIZE_IN_SQUARES; curRow++) {
            if ((curRow % 2 == 0 && color == Color.WHITE) || (curRow % 2 == 1 && color == Color.BLACK)) {
                if (highlight && validPositions.contains(curPos)) {
                    if (pos.equals(curPos)) {
                        setHighlight(out);
                    }
                    else {
                        setBlackHighlight(out);
                    }
                }
                else {
                    setBlack(out);
                }
                renderPiece(out);
            }
            else {
                if (highlight && validPositions.contains(curPos)) {
                    if (pos.equals(curPos)) {
                        setHighlight(out);
                    }
                    else {
                        setWhiteHighlight(out);
                    }
                }
                else {
                    setWhite(out);
                }
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

    private static void renderBoard(PrintStream out, Color color, boolean highlight) {
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
                renderRow(out, Color.WHITE, color, highlight);
            }
            else {
                renderRow(out, Color.BLACK, color, highlight);
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

    private static void setWhiteHighlight(PrintStream out) {
        out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREEN);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setBlackHighlight(PrintStream out) {
        out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
        out.print(EscapeSequences.SET_BG_COLOR_GREEN);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setHighlight(PrintStream out) {
        out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
        out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }
}
