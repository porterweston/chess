package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int col;
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    public void setRow(int row) { this.row = row; }

    public void setCol(int col) { this.col = col; }

    //returns if this chess position is within the bounds of the board
    public boolean isInBounds() {
        if (this.row > 8 || this.row < 1 || this.col > 8 || this.col < 1) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        String col = "";
        switch (this.col) {
            case 1 -> col = "a";
            case 2 -> col = "b";
            case 3 -> col = "c";
            case 4 -> col = "d";
            case 5 -> col = "e";
            case 6 -> col = "f";
            case 7 -> col = "g";
            case 8 -> col = "h";
        }
        return String.format("%s%d", col, this.row);
    }
}
