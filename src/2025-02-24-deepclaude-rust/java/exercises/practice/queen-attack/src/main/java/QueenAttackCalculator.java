class QueenAttackCalculator {
    private final Queen queen1;
    private final Queen queen2;

    QueenAttackCalculator(Queen queen1, Queen queen2) {
        if (queen1 == null || queen2 == null) {
            throw new IllegalArgumentException("Queens must not be null");
        }
        this.queen1 = queen1;
        this.queen2 = queen2;
    }

    boolean canQueensAttackOneAnother() {
        int rowDiff = Math.abs(queen1.row - queen2.row);
        int colDiff = Math.abs(queen1.column - queen2.column);
        return queen1.row == queen2.row || 
               queen1.column == queen2.column || 
               rowDiff == colDiff;
    }
}
