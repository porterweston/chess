package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        int[][] directions = new int[][]{{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        LineMovesCalculator calc = new LineMovesCalculator(board, position);

        return calc.calculateMoves(color, directions);
    }
}
