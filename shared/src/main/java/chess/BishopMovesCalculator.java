package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> bishopMoves = new ArrayList<ChessMove>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();

        /*
            MOVES
        */

        //up left
        ChessPosition curPosition = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        while (curPosition.getColumn() >= 1 && curPosition.getRow() <= 8) {
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) bishopMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            bishopMoves.add(new ChessMove(position, curPosition, null));
            curPosition = new ChessPosition(curPosition.getRow()+1, curPosition.getColumn()-1);
        }

        //up right
        curPosition = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        while (curPosition.getColumn() <= 8 && curPosition.getRow() <= 8) {
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) bishopMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            bishopMoves.add(new ChessMove(position, curPosition, null));
            curPosition = new ChessPosition(curPosition.getRow()+1, curPosition.getColumn()+1);
        }

        //down left
        curPosition = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        while (curPosition.getColumn() >= 1 && curPosition.getRow() >= 1) {
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) bishopMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            bishopMoves.add(new ChessMove(position, curPosition, null));
            curPosition = new ChessPosition(curPosition.getRow()-1, curPosition.getColumn()-1);
        }

        //down right
        curPosition = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        while (curPosition.getColumn() <= 8 && curPosition.getRow() >= 1) {
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) bishopMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            bishopMoves.add(new ChessMove(position, curPosition, null));
            curPosition = new ChessPosition(curPosition.getRow()-1, curPosition.getColumn()+1);
        }

        return bishopMoves;
    }
}