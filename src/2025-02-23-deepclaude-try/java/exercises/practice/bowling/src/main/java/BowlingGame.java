import java.util.ArrayList;
import java.util.List;

class BowlingGame {
    private List<Integer> rolls = new ArrayList<>();

    void roll(int pins) {
        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("Invalid pins value: " + pins);
        }
        rolls.add(pins);
    }

    int score() {
        int total = 0;
        int rollIndex = 0;
        
        for (int frame = 0; frame < 10; frame++) {
            if (isStrike(rollIndex)) {
                total += 10 + strikeBonus(rollIndex);
                rollIndex += 1;
            } else if (isSpare(rollIndex)) {
                total += 10 + spareBonus(rollIndex);
                rollIndex += 2;
            } else {
                total += sumOfFrame(rollIndex);
                rollIndex += 2;
            }
        }
        return total;
    }

    private boolean isStrike(int rollIndex) {
        return rolls.get(rollIndex) == 10;
    }

    private boolean isSpare(int rollIndex) {
        return sumOfFrame(rollIndex) == 10;
    }

    private int strikeBonus(int rollIndex) {
        return rolls.get(rollIndex + 1) + rolls.get(rollIndex + 2);
    }

    private int spareBonus(int rollIndex) {
        return rolls.get(rollIndex + 2);
    }

    private int sumOfFrame(int rollIndex) {
        return rolls.get(rollIndex) + rolls.get(rollIndex + 1);
    }
}
