public class QueenAttackCalculator {

    private final Queen queen1;
    private final Queen queen2;
    
    QueenAttackCalculator(Queen queen1, Queen queen2) {
        if (queen1.getRow() == queen2.getRow() && queen1.getColumn() == queen2.getColumn()) {
            throw new IllegalArgumentException("Queens cannot occupy the same position");
        }
        this.queen1 = queen1;
        this.queen2 = queen2;
    }

    boolean canQueensAttackOneAnother() {
        int q1Row = queen1.getRow();
        int q1Col = queen1.getColumn();
        int q2Row = queen2.getRow();
        int q2Col = queen2.getColumn();
        
        // Same row or column
        if (q1Row == q2Row || q1Col == q2Col) {
            return true;
        }
        
        // Diagonal check (absolute difference between rows equals difference between columns)
        return Math.abs(q1Row - q2Row) == Math.abs(q1Col - q2Col);
    }

}
