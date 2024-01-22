package chess;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ArrayList;

public class QueenMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> diagonalMoves = new BishopMovesCalculator().pieceMoves(board, position);
        Collection<ChessMove> orthogonalMoves = new RookMovesCalculator().pieceMoves(board, position);
        diagonalMoves.addAll(orthogonalMoves);
        return diagonalMoves;
    }
}
