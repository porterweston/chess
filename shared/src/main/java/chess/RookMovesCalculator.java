package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        int[][] directions = new int[][]{{1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        LineMovesCalculator calc = new LineMovesCalculator(board, position);

        return calc.calculateMoves(color, directions);
    }
}
