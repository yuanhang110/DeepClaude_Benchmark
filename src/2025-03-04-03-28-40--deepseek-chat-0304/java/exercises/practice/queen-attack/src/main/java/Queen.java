class Queen {
    private final int row;
    private final int column;

    Queen(int row, int column) {
        if (row < 0 || row >= 8 || column < 0 || column >= 8) {
            throw new IllegalArgumentException("Queen position must be on the chessboard");
        }
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Queen queen = (Queen) obj;
        return row == queen.row && column == queen.column;
    }

    @Override
    public int hashCode() {
        return 31 * row + column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
