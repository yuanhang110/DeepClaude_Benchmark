//
// This is only a SKELETON file for the 'Bowling' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Bowling {
  constructor() {
    this.rolls = [];
    this.currentFrame = 0;
    this.currentRoll = 0;
  }

  roll(pins) {
    if (this.currentFrame >= 10) {
      throw new Error('Cannot roll after game is over');
    }

    if (pins < 0) {
      throw new Error('Negative roll is invalid');
    }
    if (pins > 10) {
      throw new Error('Pin count exceeds pins on the lane');
    }

    if (this.currentFrame < 9) {
      if (this.currentRoll === 0) {
        if (pins === 10) {
          this.rolls.push(pins);
          this.currentFrame++;
          this.currentRoll = 0;
        } else {
          this.rolls.push(pins);
          this.currentRoll = 1;
        }
      } else {
        const previous = this.rolls[this.rolls.length - 1];
        if (previous + pins > 10) {
          throw new Error('Pin count exceeds pins on the lane');
        }
        this.rolls.push(pins);
        this.currentFrame++;
        this.currentRoll = 0;
      }
    } else {
      // Handle 10th frame
      if (this.currentRoll === 0) {
        this.rolls.push(pins);
        this.currentRoll = 1;
      } else if (this.currentRoll === 1) {
        const first = this.rolls[this.rolls.length - 1];
        if (first !== 10 && first + pins > 10) {
          throw new Error('Pin count exceeds pins on the lane');
        }
        this.rolls.push(pins);
        
        // Check if we need a third roll
        if (this.rolls[this.rolls.length - 2] === 10 || 
            (this.rolls[this.rolls.length - 2] + this.rolls[this.rolls.length - 1] === 10)) {
          this.currentRoll = 2;
        } else {
          this.currentFrame = 10;
        }
      } else if (this.currentRoll === 2) {
        const secondRoll = this.rolls[this.rolls.length - 1];
        if (secondRoll !== 10 && secondRoll + pins > 10) {
          throw new Error('Pin count exceeds pins on the lane');
        }
        this.rolls.push(pins);
        this.currentFrame = 10;
      }
    }
  }

  score() {
    if (this.currentFrame < 10) {
      throw new Error('Score cannot be taken until the end of the game');
    }

    let total = 0;
    let current = 0;

    for (let frame = 0; frame < 10; frame++) {
      if (frame === 9) {
        total += this.rolls.slice(current).reduce((a, b) => a + b, 0);
        break;
      }

      if (this.rolls[current] === 10) {
        // Strike
        total += 10 + (this.rolls[current + 1] || 0) + (this.rolls[current + 2] || 0);
        current++;
      } else {
        const first = this.rolls[current];
        const second = this.rolls[current + 1] || 0;
        if (first + second === 10) {
          // Spare
          total += 10 + (this.rolls[current + 2] || 0);
        } else {
          total += first + second;
        }
        current += 2;
      }
    }

    return total;
  }
}
