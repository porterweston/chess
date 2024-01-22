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
        ChessPosition curPosition = position;
        while (curPosition.getColumn() >= 1 && curPosition.getRow() <= 8) {
            curPosition = new ChessPosition(curPosition.getRow()+1, curPosition.getColumn()-1);
            var p = board.getPiece(curPosition);
            System.out.println(p);
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) bishopMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            bishopMoves.add(new ChessMove(position, curPosition, null));
        }

        //up right
        curPosition = position;
        while (curPosition.getColumn() <= 8 && curPosition.getRow() <= 8) {
            curPosition = new ChessPosition(curPosition.getRow()+1, curPosition.getColumn()+1);
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) bishopMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            bishopMoves.add(new ChessMove(position, curPosition, null));
        }

        //down left
        curPosition = position;
        while (curPosition.getColumn() >= 1 && curPosition.getRow() >= 1) {
            curPosition = new ChessPosition(curPosition.getRow()-1, curPosition.getColumn()-1);
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) bishopMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            bishopMoves.add(new ChessMove(position, curPosition, null));
        }

        //down right
        curPosition = position;
        while (curPosition.getColumn() <= 8 && curPosition.getRow() >= 1) {
            curPosition = new ChessPosition(curPosition.getRow()-1, curPosition.getColumn()+1);
            if (board.getPiece(curPosition) != null){
                if (board.getPiece(curPosition).getTeamColor() != team) bishopMoves.add(new ChessMove(position, curPosition, null));
                break;
            }
            bishopMoves.add(new ChessMove(position, curPosition, null));
        }

        for (ChessMove move : bishopMoves){
            System.out.println(move.getEndPosition());
        }
        return bishopMoves;
    }
}
