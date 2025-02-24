public class Queen {

    private final int row;
    private final int column;
    
    Queen(int row, int column) {
        if (row < 0 || row >= 8 || column < 0 || column >= 8) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.row = row;
        this.column = column;
    }
    
    public int getRow() { return row; }
    public int getColumn() { return column; }

}
