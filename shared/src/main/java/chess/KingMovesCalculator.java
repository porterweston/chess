package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KingMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] relPositions = new int[][]{{1, -1}, {1, 0}, {1, 1}, {0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}};

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
