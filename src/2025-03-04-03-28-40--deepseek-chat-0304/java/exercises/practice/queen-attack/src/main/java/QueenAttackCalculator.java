class QueenAttackCalculator {
    private final Queen queen1;
    private final Queen queen2;

    QueenAttackCalculator(Queen queen1, Queen queen2) {
        if (queen1 == null || queen2 == null) {
            throw new IllegalArgumentException("Queens cannot be null");
        }
        if (queen1.equals(queen2)) {
            throw new IllegalArgumentException("Queens cannot occupy the same position");
        }
        this.queen1 = new Queen(queen1.getRow(), queen1.getColumn());
        this.queen2 = new Queen(queen2.getRow(), queen2.getColumn());
    }

    boolean canQueensAttackOneAnother() {
        // Same row
        if (queen1.getRow() == queen2.getRow()) {
            return true;
        }
        // Same column
        if (queen1.getColumn() == queen2.getColumn()) {
            return true;
        }
        // Same diagonal (absolute difference between rows equals difference between columns)
        return Math.abs(queen1.getRow() - queen2.getRow()) == 
               Math.abs(queen1.getColumn() - queen2.getColumn());
    }
}
