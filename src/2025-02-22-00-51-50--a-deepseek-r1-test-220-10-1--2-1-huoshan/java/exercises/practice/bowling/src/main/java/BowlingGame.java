import java.util.ArrayList;

class BowlingGame {
    private ArrayList<Integer> rolls = new ArrayList<>();

    void roll(int pins) {
        rolls.add(pins);
    }

    int score() {
        int total = 0;
        int currentRoll = 0;
        
        for (int frame = 0; frame < 10; frame++) {
            if (isStrike(currentRoll)) {
                total += 10 + strikeBonus(currentRoll);
                currentRoll++;
            } else if (isSpare(currentRoll)) {
                total += 10 + spareBonus(currentRoll);
                currentRoll += 2;
            } else {
                total += sumOfRollsInFrame(currentRoll);
                currentRoll += 2;
            }
        }
        return total;
    }

    private boolean isStrike(int rollIndex) {
        return rollIndex < rolls.size() && rolls.get(rollIndex) == 10;
    }

    private boolean isSpare(int rollIndex) {
        return rollIndex + 1 < rolls.size() && sumOfRollsInFrame(rollIndex) == 10;
    }

    private int strikeBonus(int rollIndex) {
        int bonus = 0;
        if (rollIndex + 1 < rolls.size()) bonus += rolls.get(rollIndex + 1);
        if (rollIndex + 2 < rolls.size()) bonus += rolls.get(rollIndex + 2);
        return bonus;
    }

    private int spareBonus(int rollIndex) {
        return (rollIndex + 2 < rolls.size()) ? rolls.get(rollIndex + 2) : 0;
    }

    private int sumOfRollsInFrame(int rollIndex) {
        int sum = rolls.get(rollIndex);
        if (rollIndex + 1 < rolls.size()) sum += rolls.get(rollIndex + 1);
        return sum;
    }
}
