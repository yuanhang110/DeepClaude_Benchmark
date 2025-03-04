export class Bowling {
  constructor() {
    this.rolls = [];
    this.currentFrame = 1;
    this.frameRolls = 0;
    this.gameOver = false;
  }

  roll(pins) {
    if (this.gameOver) {
      throw new Error('Cannot roll after game is over');
    }
    if (pins < 0 || pins > 10) {
      throw new Error('Invalid number of pins');
    }

    if (this.currentFrame < 10) {
      if (this.frameRolls === 0) {
        // First roll in frame
        if (pins === 10) {
          // Strike
          this.rolls.push(pins);
          this.currentFrame++;
          this.frameRolls = 0;
        } else {
          this.rolls.push(pins);
          this.frameRolls = 1;
        }
      } else {
        // Second roll in frame
        const previousPins = this.rolls[this.rolls.length - 1];
        if (previousPins + pins > 10) {
          throw new Error('Invalid frame score (>10)');
        }
        this.rolls.push(pins);
        this.currentFrame++;
        this.frameRolls = 0;
      }
    } else {
      // Handle 10th frame
      const tenthFrameRolls = this.rolls.slice(18);
      const numRolls = tenthFrameRolls.length;

      if (numRolls >= 3) {
        throw new Error('Cannot roll after game is over');
      }

      if (numRolls === 0) {
        this.rolls.push(pins);
        if (pins === 10) { // Strike in first roll
          // Allow two more rolls
        }
      } else if (numRolls === 1) {
        const first = tenthFrameRolls[0];
        if (first === 10) { // First was strike
          this.rolls.push(pins);
          if (numRolls === 1 && pins !== 10 && this.rolls.length === 20) {
            // Second bonus roll after strike
            this.gameOver = true;
          }
        } else {
          if (first + pins > 10) {
            throw new Error('Invalid frame score (>10)');
          }
          this.rolls.push(pins);
          if (first + pins < 10) {
            this.gameOver = true;
          }
        }
      } else if (numRolls === 2) {
        this.rolls.push(pins);
        this.gameOver = true;
      }
    }

    // Check if game should be over after this roll
    if (this.currentFrame === 10) {
      const tenthFrameRolls = this.rolls.slice(18);
      const numRolls = tenthFrameRolls.length;
      const first = tenthFrameRolls[0];
      const second = tenthFrameRolls[1];
      
      if (numRolls === 2) {
        if (first === 10) { // Strike
          if (second === 10) {
            // Need third roll
          } else {
            // Two rolls after strike
            this.gameOver = (numRolls === 3);
          }
        } else if (first + second === 10) { // Spare
          // Need third roll
        } else {
          this.gameOver = true;
        }
      } else if (numRolls === 3) {
        this.gameOver = true;
      }
    }
  }

  score() {
    if (!this.gameOver) {
      throw new Error('Score cannot be taken until the end of the game');
    }

    let total = 0;
    let rollIndex = 0;

    for (let frame = 0; frame < 9; frame++) {
      const firstRoll = this.rolls[rollIndex];
      if (firstRoll === 10) { // Strike
        total += 10 + (this.rolls[rollIndex + 1] || 0) + (this.rolls[rollIndex + 2] || 0);
        rollIndex++;
      } else {
        const secondRoll = this.rolls[rollIndex + 1];
        const sum = firstRoll + secondRoll;
        if (sum === 10) { // Spare
          total += 10 + (this.rolls[rollIndex + 2] || 0);
        } else {
          total += sum;
        }
        rollIndex += 2;
      }
    }

    // Add 10th frame score (sum of all remaining rolls)
    total += this.rolls.slice(rollIndex).reduce((sum, pins) => sum + pins, 0);
    return total;
  }
}
