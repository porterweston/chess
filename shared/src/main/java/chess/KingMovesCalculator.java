package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KingMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece king = board.getPiece(position);
        ChessGame.TeamColor color = king.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        //up left
        ChessMove upLeft = new ChessMove(position, new ChessPosition(position.getRow()+1, position.getColumn()-1), null);
        if (upLeft.getEndPosition().isInBounds()){
            if (board.getPiece(upLeft.getEndPosition()) != null) {
                if (color != board.getPiece(upLeft.getEndPosition()).getTeamColor()) moves.add(upLeft);
            } else {
                moves.add(upLeft);
            }
        }

        //up
        ChessMove up = new ChessMove(position, new ChessPosition(position.getRow()+1, position.getColumn()), null);
        if (up.getEndPosition().isInBounds()){
            if (board.getPiece(up.getEndPosition()) != null) {
                if (color != board.getPiece(up.getEndPosition()).getTeamColor()) moves.add(up);
            } else {
                moves.add(up);
            }
        }

        //up right
        ChessMove upRight = new ChessMove(position, new ChessPosition(position.getRow()+1, position.getColumn()+1), null);
        if (upRight.getEndPosition().isInBounds()){
            if (board.getPiece(upRight.getEndPosition()) != null) {
                if (color != board.getPiece(upRight.getEndPosition()).getTeamColor()) moves.add(upRight);
            } else {
                moves.add(upRight);
            }
        }

        //left
        ChessMove left = new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn()-1), null);
        if (left.getEndPosition().isInBounds()){
            if (board.getPiece(left.getEndPosition()) != null) {
                if (color != board.getPiece(left.getEndPosition()).getTeamColor()) moves.add(left);
            } else {
                moves.add(left);
            }
        }

        //right
        ChessMove right = new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn()+1), null);
        if (right.getEndPosition().isInBounds()){
            if (board.getPiece(right.getEndPosition()) != null) {
                if (color != board.getPiece(right.getEndPosition()).getTeamColor()) moves.add(right);
            } else {
                moves.add(right);
            }
        }

        //down left
        ChessMove downLeft = new ChessMove(position, new ChessPosition(position.getRow()-1, position.getColumn()-1), null);
        if (downLeft.getEndPosition().isInBounds()){
            if (board.getPiece(downLeft.getEndPosition()) != null) {
                if (color != board.getPiece(downLeft.getEndPosition()).getTeamColor()) moves.add(downLeft);
            } else {
                moves.add(downLeft);
            }
        }

        //down
        ChessMove down = new ChessMove(position, new ChessPosition(position.getRow()-1, position.getColumn()), null);
        if (down.getEndPosition().isInBounds()){
            if (board.getPiece(down.getEndPosition()) != null) {
                if (color != board.getPiece(down.getEndPosition()).getTeamColor()) moves.add(down);
            } else {
                moves.add(down);
            }
        }

        //down right
        ChessMove downRight = new ChessMove(position, new ChessPosition(position.getRow()-1, position.getColumn()+1), null);
        if (downRight.getEndPosition().isInBounds()){
            if (board.getPiece(downRight.getEndPosition()) != null) {
                if (color != board.getPiece(downRight.getEndPosition()).getTeamColor()) moves.add(downRight);
            } else {
                moves.add(downRight);
            }
        }

        return moves;
    }
}
