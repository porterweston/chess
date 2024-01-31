package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private ChessGame.TeamColor teamTurn;

    public ChessGame() {
        this.teamTurn = ChessGame.TeamColor.WHITE;
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
        if (piece == null){
            return null;
        }
        else{
            //get all possible moves
            Collection<ChessMove> validMoves = piece.pieceMoves(this.board, startPosition);
            //simulate each move, if it fails then remove it from validMoves
            validMoves.removeIf(move -> !this.isValidMove(move));
            return validMoves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //check that the move is valid
        Collection<ChessMove> validMoves = this.validMoves(move.getStartPosition());
        if (!validMoves.contains(move)){
            throw new InvalidMoveException("Invalid move");
        }
        //get a reference to the piece to be moved
        ChessPiece piece = this.board.getPiece(move.getStartPosition());
        //set the piece to null
        this.board.addPiece(move.getStartPosition(), null);
        //move the piece to the move's end position
        this.board.addPiece(move.getEndPosition(), piece);
        if (move.getPromotionPiece() != null){
            this.board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //cycle through every square on the board
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                //only calculate opposing team pieces
                ChessPiece piece = this.board.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getTeamColor() != teamColor){
                    //get all valid moves of the piece on this square
                    Collection<ChessMove> validMoves = this.validMoves(new ChessPosition(i, j));
                    ChessPosition kingPosition = this.getKingPosition();
                    for (ChessMove move : validMoves){
                        if (move.getEndPosition() == kingPosition){
                            return true;
                        }
                    }
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = false;
        //cycle through every square on the board
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (this.board.getPiece(new ChessPosition(i, j)) != null){
                    //get all valid moves of the piece on this square
                    Collection<ChessMove> validMoves = this.validMoves(new ChessPosition(i, j));
                    stalemate = validMoves.isEmpty();
                }
            }
        }
        return stalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        this.board.resetBoard();
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
     * Simulates a given move to check if it's valid
     * (If the move is valid, the king is not left in check, and it's their turn)
     *
     * @param move the move to simulate
     * @return if the simulated move is valid
     */
    private boolean isValidMove(ChessMove move){
        //get the moving piece and the piece at its end position
        ChessPiece piece = this.board.getPiece(move.getStartPosition());
        ChessPiece otherPiece = this.board.getPiece(move.getEndPosition());
        //make the move
        try{
            this.makeMove(move);
        }
        catch(InvalidMoveException e){
            return false;
        }
        //check if the king was left in check or if it's their turn
        boolean isValid = !this.isInCheck(this.getTeamTurn()) && this.getTeamTurn() == piece.getTeamColor();
        //set the board back to its position before the move was made
        this.board.addPiece(move.getStartPosition(), piece);
        this.board.addPiece(move.getEndPosition(), otherPiece);
        return isValid;
    }

    /**
     * Gets the king's position on the board
     *
     * @return the king
     */
    private ChessPosition getKingPosition(){
        //cycle through every square on the board
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                ChessPiece piece = this.board.getPiece(new ChessPosition(i, j));
                if (piece != null){
                    if (piece.getPieceType() == ChessPiece.PieceType.KING) return new ChessPosition(i, j);
                }
            }
        }
    }
}
