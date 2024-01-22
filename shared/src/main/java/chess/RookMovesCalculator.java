package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> rookMoves = new ArrayList<ChessMove>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();

        /*
            MOVES
        */

        //up
        ChessPosition curPosition = new ChessPosition(position.getRow()+1, position.getColumn());
        while (curPosition.getRow() <= 8) {
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) rookMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            rookMoves.add(new ChessMove(position, curPosition, null));
            curPosition = new ChessPosition(curPosition.getRow()+1, curPosition.getColumn());
        }

        //down
        curPosition = new ChessPosition(position.getRow()-1, position.getColumn());
        while (curPosition.getRow() >= 1) {
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) rookMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            rookMoves.add(new ChessMove(position, curPosition, null));
            curPosition = new ChessPosition(curPosition.getRow()-1, curPosition.getColumn());
        }

        //left
        curPosition = new ChessPosition(position.getRow(), position.getColumn()-1);
        while (curPosition.getColumn() >= 1) {
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) rookMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            rookMoves.add(new ChessMove(position, curPosition, null));
            curPosition = new ChessPosition(curPosition.getRow(), curPosition.getColumn()-1);
        }

        //right
        curPosition = new ChessPosition(position.getRow(), position.getColumn()+1);
        while (curPosition.getColumn() <= 8) {
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) rookMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            rookMoves.add(new ChessMove(position, curPosition, null));
            curPosition = new ChessPosition(curPosition.getRow(), curPosition.getColumn()+1);
        }

        return rookMoves;
    }
}
