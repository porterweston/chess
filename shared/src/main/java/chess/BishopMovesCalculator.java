package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = new int[][]{{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};

        //calculate moves in all directions
        for (int[] direction : directions) {
            ChessPosition curPos = new ChessPosition(position.getRow() + direction[0], position.getColumn() + direction[1]);
            while (curPos.isInBounds()) {
                //if there's a piece in the next square
                if (board.getPiece(curPos) != null) {
                    if (color != board.getPiece(curPos).getTeamColor()) {
                        moves.add(new ChessMove(position, new ChessPosition(curPos.getRow(), curPos.getColumn()), null));
                    }
                    break;
                }
                //add the move
                moves.add(new ChessMove(position, new ChessPosition(curPos.getRow(), curPos.getColumn()), null));
                //update curPos
                curPos.setRow(curPos.getRow() + direction[0]);
                curPos.setCol(curPos.getColumn() + direction[1]);
            }
        }

        return moves;
    }
}
