package chess;
import java.util.Collection;

public class PieceMovesCalculator {
    private ChessPiece.PieceType type;

    PieceMovesCalculator(ChessPiece.PieceType type){
        this.type = type;
    }
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        return switch (type) {
            case ChessPiece.PieceType.PAWN -> {
                PawnMovesCalculator pawnCalculator = new PawnMovesCalculator();
                yield pawnCalculator.pieceMoves(board, position);
            }
            case ChessPiece.PieceType.KNIGHT -> {
                KnightMovesCalculator knightCalculator = new KnightMovesCalculator();
                yield knightCalculator.pieceMoves(board, position);
            }
            case ChessPiece.PieceType.BISHOP -> {
                BishopMovesCalculator bishopCalculator = new BishopMovesCalculator();
                yield bishopCalculator.pieceMoves(board, position);
            }
            case ChessPiece.PieceType.ROOK -> {
                RookMovesCalculator rookCalculator = new RookMovesCalculator();
                yield rookCalculator.pieceMoves(board, position);
            }
            case ChessPiece.PieceType.QUEEN -> {
                QueenMovesCalculator queenCalculator = new QueenMovesCalculator();
                yield queenCalculator.pieceMoves(board, position);
            }
            case ChessPiece.PieceType.KING -> {
                KingMovesCalculator kingCalculator = new KingMovesCalculator();
                yield kingCalculator.pieceMoves(board, position);
            }
        };
    }
}