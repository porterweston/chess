package chess;

import java.util.ArrayList;
import java.util.Collection;

public class LineMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;

    public LineMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> calculateMoves(ChessGame.TeamColor color, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
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
