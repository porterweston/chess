package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> knightMoves = new ArrayList<ChessMove>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();

        /*
            MOVES
        */

        //up 2 left 1
        ChessPosition endPos = new ChessPosition(position.getRow()+2, position.getColumn()-1);
        if (endPos.isInBounds()){
            ChessMove move = new ChessMove(position, endPos, null);
            if (board.getPiece(move.getEndPosition()) == null || board.getPiece(move.getEndPosition()).getTeamColor() != team) {
                knightMoves.add(move);
            }
        }

        //up 2 right 1
        endPos = new ChessPosition(position.getRow()+2, position.getColumn()+1);
        if (endPos.isInBounds()){
            ChessMove move = new ChessMove(position, endPos, null);
            if (board.getPiece(move.getEndPosition()) == null || board.getPiece(move.getEndPosition()).getTeamColor() != team) {
                knightMoves.add(move);
            }
        }

        //up 1 left 2
        endPos = new ChessPosition(position.getRow()+1, position.getColumn()-2);
        if (endPos.isInBounds()){
            ChessMove move = new ChessMove(position, endPos, null);
            if (board.getPiece(move.getEndPosition()) == null || board.getPiece(move.getEndPosition()).getTeamColor() != team) {
                knightMoves.add(move);
            }
        }

        //up 1 right 2
        endPos = new ChessPosition(position.getRow()+1, position.getColumn()+2);
        if (endPos.isInBounds()){
            ChessMove move = new ChessMove(position, endPos, null);
            if (board.getPiece(move.getEndPosition()) == null || board.getPiece(move.getEndPosition()).getTeamColor() != team) {
                knightMoves.add(move);
            }
        }

        //down 1 left 2
        endPos = new ChessPosition(position.getRow()-1, position.getColumn()-2);
        if (endPos.isInBounds()){
            ChessMove move = new ChessMove(position, endPos, null);
            if (board.getPiece(move.getEndPosition()) == null || board.getPiece(move.getEndPosition()).getTeamColor() != team) {
                knightMoves.add(move);
            }
        }

        //down 1 right 2
        endPos = new ChessPosition(position.getRow()-1, position.getColumn()+2);
        if (endPos.isInBounds()){
            ChessMove move = new ChessMove(position, endPos, null);
            if (board.getPiece(move.getEndPosition()) == null || board.getPiece(move.getEndPosition()).getTeamColor() != team) {
                knightMoves.add(move);
            }
        }

        //down 2 left 1
        endPos = new ChessPosition(position.getRow()-2, position.getColumn()-1);
        if (endPos.isInBounds()){
            ChessMove move = new ChessMove(position, endPos, null);
            if (board.getPiece(move.getEndPosition()) == null || board.getPiece(move.getEndPosition()).getTeamColor() != team) {
                knightMoves.add(move);
            }
        }

        //down 2 right 1
        endPos = new ChessPosition(position.getRow()-2, position.getColumn()+1);
        if (endPos.isInBounds()){
            ChessMove move = new ChessMove(position, endPos, null);
            if (board.getPiece(move.getEndPosition()) == null || board.getPiece(move.getEndPosition()).getTeamColor() != team) {
                knightMoves.add(move);
            }
        }


        return knightMoves;
    }
}
