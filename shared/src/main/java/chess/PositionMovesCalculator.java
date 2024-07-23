package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PositionMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;

    public PositionMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> calculateMoves(ChessGame.TeamColor color, int[][] relPositions) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        for (int[] relPos : relPositions) {
            ChessPosition endPos = new ChessPosition(position.getRow() + relPos[0], position.getColumn() + relPos[1]);
            if (endPos.isInBounds()) {
                if (board.getPiece(endPos) != null) {
                    if (color != board.getPiece(endPos).getTeamColor()) {
                        moves.add(new ChessMove(position, endPos, null));
                    }
                }
                else {
                    moves.add(new ChessMove(position, endPos, null));
                }
            }
        }

        return moves;
    }
}
