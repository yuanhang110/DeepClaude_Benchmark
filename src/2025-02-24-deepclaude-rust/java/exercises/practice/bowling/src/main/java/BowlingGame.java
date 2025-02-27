import java.util.ArrayList;
import java.util.List;

class BowlingGame {
    private List<Integer> rolls = new ArrayList<>();

    void roll(int pins) {
        rolls.add(pins);
    }

    int score() {
        int totalScore = 0;
        int currentRoll = 0;
        
        for (int frame = 0; frame < 10; frame++) {
            if (frame == 9) { // 10th frame
                int frameScore = 0;
                int maxRolls = Math.min(currentRoll + 3, rolls.size());
                for (int i = currentRoll; i < maxRolls; i++) {
                    frameScore += rolls.get(i);
                }
                totalScore += frameScore;
                break;
            }
            
            if (isStrike(currentRoll)) {
                totalScore += 10 + strikeBonus(currentRoll);
                currentRoll++;
            } else if (isSpare(currentRoll)) {
                totalScore += 10 + rolls.get(currentRoll + 2);
                currentRoll += 2;
            } else {
                totalScore += sumOfTwoBalls(currentRoll);
                currentRoll += 2;
            }
        }
        
        return totalScore;
    }
    
    private boolean isStrike(int roll) {
        return rolls.get(roll) == 10;
    }
    
    private boolean isSpare(int roll) {
        return sumOfTwoBalls(roll) == 10;
    }
    
    private int strikeBonus(int roll) {
        return sumOfTwoBalls(roll + 1);
    }
    
    private int sumOfTwoBalls(int roll) {
        if (roll + 1 >= rolls.size()) return rolls.get(roll);
        return rolls.get(roll) + rolls.get(roll + 1);
    }
}
