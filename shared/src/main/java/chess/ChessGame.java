package chess;

import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;

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
        ChessPiece piece = this.board.getPiece(startPosition);
        if (piece == null) return null;
        Collection<ChessMove> pieceMoves = this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
        //iterate through all moves in piece moves
        for (ChessMove move : pieceMoves) {
            ChessBoard boardSim = this.board.copy();
            movePiece(move, boardSim);
            if (!this.boardInCheck(piece.getTeamColor(), boardSim)) {
                validMoves.add(move);
            }
        }
        return validMoves;
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

        //make the move
        this.movePiece(move, this.board);

        //change the turn
        this.setTeamTurn(this.swapTurn(this.teamTurn));
    }

    /**
     * Moves a chess piece on a given board
     * @param move The move to perform
     */
    private void movePiece(ChessMove move, ChessBoard board) {
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
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return this.boardInCheck(teamColor, this.board);
    }

    /**
     * Returns if a given team is in check on a given board
     * @param teamColor The team to see if they're in check
     * @param board The board in which to check
     * @return if the given team is in check on the given board
     */
    private boolean boardInCheck(TeamColor teamColor, ChessBoard board) {
        Map<ChessPosition, ChessPiece> opposingPieces = board.getPieces(this.swapTurn(teamColor));
        ChessPosition kingPos = board.findKing(teamColor);
        //loop through every piece's position of the opposing team
        for (ChessPosition position : opposingPieces.keySet()) {
            Collection<ChessMove> moves = board.getPiece(position).pieceMoves(board, position);
            //loop through every move of the piece at the current position
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
        Map<ChessPosition, ChessPiece> myPieces = this.board.getPieces(teamColor);
        ChessPosition kingPos = this.board.findKing(teamColor);
        if (!this.isInCheck(teamColor)) return false;
        //loop through every piece's position of my team
        for (ChessPosition position : myPieces.keySet()) {
            //loop through every move of the piece at the current position
            Collection<ChessMove> moves = this.board.getPiece(position).pieceMoves(this.board, position);
            for (ChessMove move : moves) {
                //simulate the move
                ChessBoard boardSim = this.board.copy();
                this.movePiece(move, boardSim);
                if (!this.boardInCheck(teamColor, boardSim)) return false;
            }
        }
        return true;
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
