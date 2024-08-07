package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //determine which calculator to use, and return the moves using that calculator
        return switch (this.type) {
            case PieceType.PAWN -> {
                PawnMovesCalculator pawnCalc = new PawnMovesCalculator();
                yield pawnCalc.pieceMoves(board, myPosition);
            }
            case PieceType.KNIGHT -> {
                KnightMovesCalculator knightCalc = new KnightMovesCalculator();
                yield knightCalc.pieceMoves(board, myPosition);
            }
            case PieceType.BISHOP -> {
                BishopMovesCalculator bishopCalc = new BishopMovesCalculator();
                yield bishopCalc.pieceMoves(board, myPosition);
            }
            case PieceType.ROOK -> {
                RookMovesCalculator rookCalc = new RookMovesCalculator();
                yield rookCalc.pieceMoves(board, myPosition);
            }
            case PieceType.QUEEN -> {
                QueenMovesCalculator queenCalc = new QueenMovesCalculator();
                yield queenCalc.pieceMoves(board, myPosition);
            }
            case PieceType.KING -> {
                KingMovesCalculator kingCalc = new KingMovesCalculator();
                yield kingCalc.pieceMoves(board, myPosition);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.pieceColor.toString(), this.type.toString());
    }
}
