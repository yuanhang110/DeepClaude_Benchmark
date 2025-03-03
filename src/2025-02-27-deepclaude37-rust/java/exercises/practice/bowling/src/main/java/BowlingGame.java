import java.util.ArrayList;
import java.util.List;

class BowlingGame {
    private List<Integer> rolls = new ArrayList<>();

    void roll(int pins) {
        rolls.add(pins);
    }

    int score() {
        int total = 0;
        int frameIndex = 0;
        
        for (int frame = 0; frame < 10; frame++) {
            if (frameIndex >= rolls.size()) {
                break;
            }
            
            if (rolls.get(frameIndex) == 10) { // Strike
                total += 10;
                if (frameIndex + 1 < rolls.size()) {
                    total += rolls.get(frameIndex + 1);
                    if (frameIndex + 2 < rolls.size()) {
                        total += rolls.get(frameIndex + 2);
                    }
                }
                frameIndex++;
            } else if (frameIndex + 1 < rolls.size() && 
                      rolls.get(frameIndex) + rolls.get(frameIndex + 1) == 10) { // Spare
                total += 10;
                if (frameIndex + 2 < rolls.size()) {
                    total += rolls.get(frameIndex + 2);
                }
                frameIndex += 2;
            } else { // Open frame
                int frameScore = rolls.get(frameIndex);
                if (frameIndex + 1 < rolls.size()) {
                    frameScore += rolls.get(frameIndex + 1);
                }
                total += frameScore;
                frameIndex += 2;
            }
        }
        
        return total;
    }

    // No helper methods needed
}
