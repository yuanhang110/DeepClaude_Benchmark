public class BowlingGame {
    private int[] rolls = new int[21];
    private int currentRoll = 0;

    public BowlingGame() {
        // Initialize the rolls array
        for (int i = 0; i < rolls.length; i++) {
            rolls[i] = -1;
        }
    }

    public void roll(int pins) {
        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("Invalid number of pins");
        }
        if (currentRoll >= rolls.length) {
            throw new IllegalStateException("Cannot roll after game is over");
        }
        rolls[currentRoll++] = pins;
    }

    public int score() {
        int score = 0;
        int frameIndex = 0;
        
        for (int frame = 0; frame < 10; frame++) {
            if (isStrike(frameIndex)) {
                score += 10 + strikeBonus(frameIndex);
                frameIndex++;
            } else if (isSpare(frameIndex)) {
                score += 10 + spareBonus(frameIndex);
                frameIndex += 2;
            } else {
                score += sumOfBallsInFrame(frameIndex);
                frameIndex += 2;
            }
        }
        return score;
    }

    private boolean isStrike(int frameIndex) {
        return rolls[frameIndex] == 10;
    }

    private boolean isSpare(int frameIndex) {
        return rolls[frameIndex] + rolls[frameIndex + 1] == 10;
    }

    private int strikeBonus(int frameIndex) {
        if (frameIndex + 2 >= rolls.length || rolls[frameIndex + 1] == -1 || rolls[frameIndex + 2] == -1) {
            throw new IllegalStateException("Incomplete game - cannot calculate strike bonus");
        }
        return rolls[frameIndex + 1] + rolls[frameIndex + 2];
    }

    private int spareBonus(int frameIndex) {
        if (frameIndex + 2 >= rolls.length || rolls[frameIndex + 2] == -1) {
            throw new IllegalStateException("Incomplete game - cannot calculate spare bonus");
        }
        return rolls[frameIndex + 2];
    }

    private int sumOfBallsInFrame(int frameIndex) {
        if (frameIndex + 1 >= rolls.length || rolls[frameIndex] == -1 || rolls[frameIndex + 1] == -1) {
            throw new IllegalStateException("Incomplete game - cannot calculate frame score");
        }
        return rolls[frameIndex] + rolls[frameIndex + 1];
    }
}
