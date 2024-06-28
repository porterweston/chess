package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        BishopMovesCalculator bishopCalc = new BishopMovesCalculator();
        RookMovesCalculator rookCalc = new RookMovesCalculator();
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        moves.addAll(bishopCalc.pieceMoves(board, position));
        moves.addAll(rookCalc.pieceMoves(board, position));

        return moves;
    }
}
