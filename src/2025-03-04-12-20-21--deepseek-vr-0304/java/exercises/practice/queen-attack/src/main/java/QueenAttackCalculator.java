class QueenAttackCalculator {
    private final Queen queen1;
    private final Queen queen2;

    QueenAttackCalculator(Queen queen1, Queen queen2) {
        if (queen1 == null || queen2 == null) {
            throw new IllegalArgumentException("Queens cannot be null");
        }
        this.queen1 = queen1;
        this.queen2 = queen2;
    }

    boolean canQueensAttackOneAnother() {
        int rowDiff = Math.abs(queen1.getRow() - queen2.getRow());
        int colDiff = Math.abs(queen1.getColumn() - queen2.getColumn());
        
        return rowDiff == 0 || colDiff == 0 || rowDiff == colDiff;
    }
}
