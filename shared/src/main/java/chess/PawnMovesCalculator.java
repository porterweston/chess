package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece pawn = board.getPiece(position);
        ChessGame.TeamColor color = pawn.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int dir = 0;
        if (color == ChessGame.TeamColor.WHITE) dir = 1;
        if (color == ChessGame.TeamColor.BLACK) dir = -1;

        //forward one
        ChessMove forwardOne = new ChessMove(position, new ChessPosition(position.getRow()+dir, position.getColumn()), null);
        if (board.getPiece(forwardOne.getEndPosition()) == null){
            if (this.promotion(forwardOne.getEndPosition(), color)){
                moves.add(new ChessMove(position, forwardOne.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(position, forwardOne.getEndPosition(), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(position, forwardOne.getEndPosition(), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(position, forwardOne.getEndPosition(), ChessPiece.PieceType.QUEEN));

            } else {
                moves.add(forwardOne);
            }
        }

        //forward two
        ChessMove forwardTwo =  new ChessMove(position, new ChessPosition(position.getRow()+(dir*2), position.getColumn()), null);
        if ((color == ChessGame.TeamColor.WHITE && position.getRow() == 2) || (color == ChessGame.TeamColor.BLACK && position.getRow() == 7)){
            if (board.getPiece(forwardTwo.getEndPosition()) == null &&
                board.getPiece(forwardOne.getEndPosition()) == null){
                moves.add(forwardTwo);
            }
        }

        //capture left
        ChessMove captureLeft = new ChessMove(position, new ChessPosition(position.getRow()+dir, position.getColumn()-1), null);
        if (captureLeft.getEndPosition().isInBounds()) {
            if (board.getPiece(captureLeft.getEndPosition()) != null && color != board.getPiece(captureLeft.getEndPosition()).getTeamColor()) {
                if (promotion(captureLeft.getEndPosition(), color)) {
                    moves.add(new ChessMove(position, captureLeft.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, captureLeft.getEndPosition(), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, captureLeft.getEndPosition(), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, captureLeft.getEndPosition(), ChessPiece.PieceType.QUEEN));
                } else {
                    moves.add(captureLeft);
                }
            }
        }

        //capture right
        ChessMove captureRight = new ChessMove(position, new ChessPosition(position.getRow()+dir, position.getColumn()+1), null);
        if (captureRight.getEndPosition().isInBounds()) {
            if (board.getPiece(captureRight.getEndPosition()) != null && color != board.getPiece(captureRight.getEndPosition()).getTeamColor()) {
                if (promotion(captureRight.getEndPosition(), color)) {
                    moves.add(new ChessMove(position, captureRight.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, captureRight.getEndPosition(), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, captureRight.getEndPosition(), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, captureRight.getEndPosition(), ChessPiece.PieceType.QUEEN));
                } else {
                    moves.add(captureRight);
                }
            }
        }

        return moves;
    }

    //returns if a pawn landing at a given position would result in a promotion
    private boolean promotion(ChessPosition position, ChessGame.TeamColor color){
        if ((color == ChessGame.TeamColor.WHITE && position.getRow() == 8) || (color == ChessGame.TeamColor.BLACK && position.getRow() == 1)){
            return true;
        }
        return false;
    }
}
