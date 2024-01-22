package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PawnMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> pawnMoves = new ArrayList<ChessMove>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();
        int dir = 0;
        if (team == ChessGame.TeamColor.WHITE) dir = 1;
        if (team == ChessGame.TeamColor.BLACK) dir = -1;

        /*
            MOVES
        */

        //forward one
        ChessMove forwardOne = new ChessMove(position, new ChessPosition(position.getRow()+(dir), position.getColumn()), null);
        if (board.getPiece(forwardOne.getEndPosition()) == null){
            if (promotion(forwardOne.getEndPosition(), team)){
                pawnMoves.add(new ChessMove(position, forwardOne.getEndPosition(), ChessPiece.PieceType.ROOK));
                pawnMoves.add(new ChessMove(position, forwardOne.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                pawnMoves.add(new ChessMove(position, forwardOne.getEndPosition(), ChessPiece.PieceType.BISHOP));
                pawnMoves.add(new ChessMove(position, forwardOne.getEndPosition(), ChessPiece.PieceType.QUEEN));
            }
            else{
                pawnMoves.add(forwardOne);
            }
        }

        //forward two
        ChessMove forwardTwo = new ChessMove(position, new ChessPosition(position.getRow()+(2*dir), position.getColumn()), null);
        //if in starting position and unobstructed, add forwardTwo
        if (((position.getRow() == 2 && team == ChessGame.TeamColor.WHITE) || (position.getRow() == 7 && team == ChessGame.TeamColor.BLACK)) &&
                board.getPiece(forwardOne.getEndPosition()) == null &&
                board.getPiece(forwardTwo.getEndPosition()) == null){
            pawnMoves.add(forwardTwo);
        }

        //capture right
        ChessPosition endPos = new ChessPosition(position.getRow()+(dir), position.getColumn()+1);
        if (endPos.isInBounds()){
            ChessMove captureRight = new ChessMove(position, endPos, null);
            if (board.getPiece(captureRight.getEndPosition()) != null && board.getPiece(captureRight.getEndPosition()).getTeamColor() != team){
                if (promotion(captureRight.getEndPosition(), team)){
                    pawnMoves.add(new ChessMove(position, captureRight.getEndPosition(), ChessPiece.PieceType.ROOK));
                    pawnMoves.add(new ChessMove(position, captureRight.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                    pawnMoves.add(new ChessMove(position, captureRight.getEndPosition(), ChessPiece.PieceType.BISHOP));
                    pawnMoves.add(new ChessMove(position, captureRight.getEndPosition(), ChessPiece.PieceType.QUEEN));
                }
                else{
                    pawnMoves.add(captureRight);
                }
            }
        }

        //capture left
        endPos = new ChessPosition(position.getRow()+(dir), position.getColumn()-1);
        if (endPos.isInBounds()){
            ChessMove captureLeft = new ChessMove(position, endPos, null);
            if (board.getPiece(captureLeft.getEndPosition()) != null && board.getPiece(captureLeft.getEndPosition()).getTeamColor() != team){
                if (promotion(captureLeft.getEndPosition(), team)){
                    pawnMoves.add(new ChessMove(position, captureLeft.getEndPosition(), ChessPiece.PieceType.ROOK));
                    pawnMoves.add(new ChessMove(position, captureLeft.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                    pawnMoves.add(new ChessMove(position, captureLeft.getEndPosition(), ChessPiece.PieceType.BISHOP));
                    pawnMoves.add(new ChessMove(position, captureLeft.getEndPosition(), ChessPiece.PieceType.QUEEN));
                }
                else{
                    pawnMoves.add(captureLeft);
                }
            }
        }

        return pawnMoves;
    }

    private boolean promotion(ChessPosition endPosition, ChessGame.TeamColor team){
        if ((endPosition.getRow() == 8 && team == ChessGame.TeamColor.WHITE) ||
            (endPosition.getRow() == 1 && team == ChessGame.TeamColor.BLACK)){
            return true;
        }
        return false;
    }
}
