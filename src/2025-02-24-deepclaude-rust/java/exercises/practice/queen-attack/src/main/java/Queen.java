class Queen {
    final int row;
    final int column;

    Queen(int row, int column) {
        if (row < 0 || row >= 8 || column < 0 || column >= 8) {
            throw new UnsupportedOperationException("Invalid position");
        }
        this.row = row;
        this.column = column;
    }
}
