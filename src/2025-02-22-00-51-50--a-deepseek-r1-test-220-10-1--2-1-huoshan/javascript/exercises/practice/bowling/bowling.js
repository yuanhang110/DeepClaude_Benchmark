//
// This is only a SKELETON file for the 'Bowling' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Bowling {
  constructor() {
    this.rolls = [];
    this.currentFrame = 1;
    this.gameDone = false;
  }

  roll(pins) {
    if (this.gameDone) {
      throw new Error('Cannot roll after game is over');
    }
    if (pins < 0) {
      throw new Error('Negative roll is invalid');
    }
    if (pins > 10) {
      throw new Error('Pin count exceeds pins on the lane');
    }

    // Handle frame transitions and validations
    if (this.currentFrame < 10) {
      const frameRolls = this.rolls.filter(r => r.frame === this.currentFrame);
      
      if (frameRolls.length === 0) { // First roll in frame
        if (pins === 10) { // Strike
          this.rolls.push({ frame: this.currentFrame, pins });
          this.currentFrame++;
        } else {
          this.rolls.push({ frame: this.currentFrame, pins });
        }
      } else { // Second roll in frame
        if (frameRolls[0].pins + pins > 10) {
          throw new Error('Pin count exceeds pins on the lane');
        }
        this.rolls.push({ frame: this.currentFrame, pins });
        this.currentFrame++;
      }
    } else { // 10th frame handling
      const frameRolls = this.rolls.filter(r => r.frame === 10);
      
      if (frameRolls.length >= 3) {
        throw new Error('Cannot roll more than 3 times in 10th frame');
      }
      
      if (frameRolls.length === 0) { // First 10th frame roll
        this.rolls.push({ frame: 10, pins });
      } else if (frameRolls.length === 1) { // Second 10th frame roll
        if (frameRolls[0].pins === 10) { // First was strike
          this.rolls.push({ frame: 10, pins });
        } else if (frameRolls[0].pins + pins > 10) {
          throw new Error('Frame score cannot exceed 10');
        } else {
          this.rolls.push({ frame: 10, pins });
          // For non-strike first roll, if no spare, game ends after second roll
          if (frameRolls[0].pins + pins < 10) {
            this.gameDone = true;
          }
        }
      } else { // Third 10th frame roll
        // Validate bonus rolls after strike
        if (frameRolls[0].pins === 10) { // First was strike
          if (frameRolls[1].pins !== 10 && frameRolls[1].pins + pins > 10) {
            throw new Error('Pin count exceeds pins on the lane');
          }
        }
        this.rolls.push({ frame: 10, pins });
        this.gameDone = true;
      }
    }

    // Auto-advance game state if we complete all frames
    if (this.currentFrame > 10) {
      this.gameDone = true;
    }
  }

  score() {
    if (!this.gameDone) {
      throw new Error('Score cannot be taken until the end of the game');
    }

    let total = 0;
    for (let frame = 1; frame <= 10; frame++) {
      const frameRolls = this.rolls.filter(r => r.frame === frame);
      
      if (frame < 10) {
        if (frameRolls[0].pins === 10) { // Strike
          total += 10 + this.#getStrikeBonus(frame);
        } else if (frameRolls.length > 1 && frameRolls[0].pins + frameRolls[1].pins === 10) { // Spare
          total += 10 + this.#getSpareBonus(frame);
        } else { // Open frame
          total += frameRolls.reduce((sum, roll) => sum + roll.pins, 0);
        }
      } else { // 10th frame just sums all rolls
        total += frameRolls.reduce((sum, roll) => sum + roll.pins, 0);
      }
    }
    return total;
  }

  #getStrikeBonus(frame) {
    const nextRolls = this.rolls.filter(r => r.frame > frame);
    return nextRolls.slice(0, 2).reduce((sum, roll) => sum + roll.pins, 0);
  }

  #getSpareBonus(frame) {
    const nextRolls = this.rolls.filter(r => r.frame > frame);
    return nextRolls.length > 0 ? nextRolls[0].pins : 0;
  }
}
