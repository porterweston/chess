package chess;

import java.util.Collection;
import java.util.Map;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //check to see if this move is a valid move
        Collection<ChessMove> validMoves = this.validMoves(move.getStartPosition());
        for (ChessMove m : validMoves) {
            if (!m.equals(move)){
                throw new InvalidMoveException("Invalid move");
            }
        }

        //move the piece
        ChessPiece piece = board.getPiece(move.getStartPosition());
        ChessPiece newPiece = null;
        if (move.getPromotionPiece() != null) {
            newPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        else {
            newPiece =  new ChessPiece(piece.getTeamColor(), piece.getPieceType());
        }
        board.addPiece(move.getStartPosition(), null);
        board.addPiece(move.getEndPosition(), newPiece);

        //change the turn
        this.setTeamTurn(this.swapTurn(this.teamTurn));
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Map<ChessPiece, ChessPosition> opposingPieces = this.board.getPieces(this.swapTurn(teamColor));
        ChessPosition kingPos = this.board.findKing(teamColor);
        //loop through every piece of the opposing team
        for (ChessPiece piece : opposingPieces.keySet()) {
            Collection<ChessMove> moves = piece.pieceMoves(this.board, opposingPieces.get(piece));
            //loop through every move of the current piece
            for (ChessMove move : moves) {
                if (move.getEndPosition().equals(kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Map<ChessPiece, ChessPosition> myPieces = this.board.getPieces(teamColor);
        ChessPosition kingPos = this.board.findKing(teamColor);
        if (!this.isInCheck(teamColor)) return false;
        //loop through every piece of my team
        for (ChessPiece piece : myPieces.keySet()) {
            //loop through every move of the current piece
            Collection<ChessMove> moves = piece.pieceMoves(this.board, myPieces.get(piece));
            for (ChessMove move : moves) {
                //simulate the move
                ChessBoard boardSim = this.board;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    /**
     * Returns the opposite of a given team's color
     */
    private TeamColor swapTurn(TeamColor teamTurn) {
        if (teamTurn == TeamColor.WHITE) return TeamColor.BLACK;
        if (teamTurn == TeamColor.BLACK) return TeamColor.WHITE;
        return null;
    }
}
