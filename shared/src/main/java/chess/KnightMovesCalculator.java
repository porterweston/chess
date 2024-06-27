package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece knight = board.getPiece(position);
        ChessGame.TeamColor color = knight.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        //up left
        ChessMove upLeft = new ChessMove(position, new ChessPosition(position.getRow()+2, position.getColumn()-1), null);
        if (upLeft.getEndPosition().isInBounds()) {
            if (board.getPiece(upLeft.getEndPosition()) != null) {
                if (color != board.getPiece(upLeft.getEndPosition()).getTeamColor()) moves.add(upLeft);
            } else {
                moves.add(upLeft);
            }
        }

        //up right
        ChessMove upRight = new ChessMove(position, new ChessPosition(position.getRow()+2, position.getColumn()+1), null);
        if (upRight.getEndPosition().isInBounds()) {
            if (board.getPiece(upRight.getEndPosition()) != null) {
                if (color != board.getPiece(upRight.getEndPosition()).getTeamColor()) moves.add(upRight);
            } else {
                moves.add(upRight);
            }
        }

        //down left
        ChessMove downLeft = new ChessMove(position, new ChessPosition(position.getRow()-2, position.getColumn()-1), null);
        if (downLeft.getEndPosition().isInBounds()) {
            if (board.getPiece(downLeft.getEndPosition()) != null) {
                if (color != board.getPiece(downLeft.getEndPosition()).getTeamColor()) moves.add(downLeft);
            } else {
                moves.add(downLeft);
            }
        }

        //down right
        ChessMove downRight = new ChessMove(position, new ChessPosition(position.getRow()-2, position.getColumn()+1), null);
        if (downRight.getEndPosition().isInBounds()) {
            if (board.getPiece(downRight.getEndPosition()) != null) {
                if (color != board.getPiece(downRight.getEndPosition()).getTeamColor()) moves.add(downRight);
            } else {
                moves.add(downRight);
            }
        }

        //left up
        ChessMove leftUp = new ChessMove(position, new ChessPosition(position.getRow()+1, position.getColumn()-2), null);
        if (leftUp.getEndPosition().isInBounds()) {
            if (board.getPiece(leftUp.getEndPosition()) != null) {
                if (color != board.getPiece(leftUp.getEndPosition()).getTeamColor()) moves.add(leftUp);
            } else {
                moves.add(leftUp);
            }
        }

        //left down
        ChessMove leftDown = new ChessMove(position, new ChessPosition(position.getRow()-1, position.getColumn()-2), null);
        if (leftDown.getEndPosition().isInBounds()) {
            if (board.getPiece(leftDown.getEndPosition()) != null) {
                if (color != board.getPiece(leftDown.getEndPosition()).getTeamColor()) moves.add(leftDown);
            } else {
                moves.add(leftDown);
            }
        }

        //right up
        ChessMove rightUp = new ChessMove(position, new ChessPosition(position.getRow()+1, position.getColumn()+2), null);
        if (rightUp.getEndPosition().isInBounds()) {
            if (board.getPiece(rightUp.getEndPosition()) != null) {
                if (color != board.getPiece(rightUp.getEndPosition()).getTeamColor()) moves.add(rightUp);
            } else {
                moves.add(rightUp);
            }
        }

        //right down
        ChessMove rightDown = new ChessMove(position, new ChessPosition(position.getRow()-1, position.getColumn()+2), null);
        if (rightDown.getEndPosition().isInBounds()) {
            if (board.getPiece(rightDown.getEndPosition()) != null) {
                if (color != board.getPiece(rightDown.getEndPosition()).getTeamColor()) moves.add(rightDown);
            } else {
                moves.add(rightDown);
            }
        }

        return moves;
    }
}
