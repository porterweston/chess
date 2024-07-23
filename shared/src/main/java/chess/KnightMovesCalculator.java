package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        int[][] relPositions = new int[][]{{2, -1}, {2, 1}, {-2, -1}, {-2, 1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
        PositionMovesCalculator calc = new PositionMovesCalculator(board, position);

        return calc.calculateMoves(color, relPositions);
    }
}
