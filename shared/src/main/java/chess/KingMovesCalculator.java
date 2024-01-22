package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KingMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> kingMoves = new ArrayList<ChessMove>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();

        /*
            MOVES
        */


        //up left
        ChessPosition endPos = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        if (endPos.isInBounds()){
            ChessMove upLeft = new ChessMove(position, endPos, null);
            if (board.getPiece(upLeft.getEndPosition()) == null || board.getPiece(upLeft.getEndPosition()).getTeamColor() != team) {
                kingMoves.add(upLeft);
            }
        }

        //up
        endPos = new ChessPosition(position.getRow()+1, position.getColumn());
        if (endPos.isInBounds()){
            ChessMove up = new ChessMove(position, endPos, null);
            if (board.getPiece(up.getEndPosition()) == null || board.getPiece(up.getEndPosition()).getTeamColor() != team) {
                kingMoves.add(up);
            }
        }

        //up right
        endPos = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        if (endPos.isInBounds()){
            ChessMove upRight = new ChessMove(position, endPos, null);
            if (board.getPiece(upRight.getEndPosition()) == null || board.getPiece(upRight.getEndPosition()).getTeamColor() != team) {
                kingMoves.add(upRight);
            }
        }

        //left
        endPos = new ChessPosition(position.getRow(), position.getColumn()-1);
        if (endPos.isInBounds()){
            ChessMove left = new ChessMove(position, endPos, null);
            if (board.getPiece(left.getEndPosition()) == null || board.getPiece(left.getEndPosition()).getTeamColor() != team) {
                kingMoves.add(left);
            }
        }

        //right
        endPos = new ChessPosition(position.getRow(), position.getColumn()+1);
        if (endPos.isInBounds()){
            ChessMove right = new ChessMove(position, endPos, null);
            if (board.getPiece(right.getEndPosition()) == null || board.getPiece(right.getEndPosition()).getTeamColor() != team) {
                kingMoves.add(right);
            }
        }

        //down left
        endPos = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        if (endPos.isInBounds()){
            ChessMove downLeft = new ChessMove(position, endPos, null);
            if (board.getPiece(downLeft.getEndPosition()) == null || board.getPiece(downLeft.getEndPosition()).getTeamColor() != team) {
                kingMoves.add(downLeft);
            }
        }

        //down
        endPos = new ChessPosition(position.getRow()-1, position.getColumn());
        if (endPos.isInBounds()){
            ChessMove down = new ChessMove(position, endPos, null);
            if (board.getPiece(down.getEndPosition()) == null || board.getPiece(down.getEndPosition()).getTeamColor() != team) {
                kingMoves.add(down);
            }
        }

        //down right
        endPos = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        if (endPos.isInBounds()){
            ChessMove downRight = new ChessMove(position, endPos, null);
            if (board.getPiece(downRight.getEndPosition()) == null || board.getPiece(downRight.getEndPosition()).getTeamColor() != team) {
                kingMoves.add(downRight);
            }
        }

        return kingMoves;
    }
}
