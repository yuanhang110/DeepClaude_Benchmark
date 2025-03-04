export class Bowling {
  constructor() {
    this.rolls = [];
    this.currentFrame = 1;
    this.isGameOver = false;
  }

  roll(pins) {
    if (this.isGameOver) {
      throw new Error('Cannot roll after game is over');
    }
    
    if (pins < 0) {
      throw new Error('Negative roll is invalid');
    }
    if (pins > 10) {
      throw new Error('Pin count exceeds pins on the lane');
    }

    // Validate frame rules
    if (this.currentFrame < 10) {
      const isSecondRoll = this.rolls.length % 2 === 1;
      if (isSecondRoll && this.rolls[this.rolls.length - 1] + pins > 10) {
        throw new Error('Pin count exceeds pins on the lane');
      }
    } else {
      // 10th frame validation
      const tenthFrameRolls = this.rolls.slice(18);
      if (tenthFrameRolls.length >= 2 && 
          tenthFrameRolls[0] + tenthFrameRolls[1] < 10) {
        this.isGameOver = true;
      }
      if (tenthFrameRolls.length === 3) {
        this.isGameOver = true;
      }
      if (tenthFrameRolls.length === 1 && tenthFrameRolls[0] !== 10) {
        if (pins + tenthFrameRolls[0] > 10) {
          throw new Error('Pin count exceeds pins on the lane');
        }
      }
      if (tenthFrameRolls.length === 2 && tenthFrameRolls[0] === 10) {
        if (tenthFrameRolls[1] !== 10 && tenthFrameRolls[1] + pins > 10) {
          throw new Error('Pin count exceeds pins on the lane');
        }
      }
    }

    this.rolls.push(pins);
    if (this.currentFrame < 10 && (pins === 10 || this.rolls.length % 2 === 0)) {
      this.currentFrame++;
    }
  }

  score() {
    if (!this.isGameOver) {
      throw new Error('Score cannot be taken until the end of the game');
    }

    let score = 0;
    let frameIndex = 0;

    for (let frame = 0; frame < 10; frame++) {
      if (this.isStrike(frameIndex)) {
        score += 10 + this.strikeBonus(frameIndex);
        frameIndex++;
      } else if (this.isSpare(frameIndex)) {
        score += 10 + this.spareBonus(frameIndex);
        frameIndex += 2;
      } else {
        score += this.sumOfBallsInFrame(frameIndex);
        frameIndex += 2;
      }
    }

    return score;
  }

  isStrike(frameIndex) {
    return this.rolls[frameIndex] === 10;
  }

  isSpare(frameIndex) {
    return this.rolls[frameIndex] + this.rolls[frameIndex + 1] === 10;
  }

  sumOfBallsInFrame(frameIndex) {
    return this.rolls[frameIndex] + this.rolls[frameIndex + 1];
  }

  strikeBonus(frameIndex) {
    return this.rolls[frameIndex + 1] + this.rolls[frameIndex + 2];
  }

  spareBonus(frameIndex) {
    return this.rolls[frameIndex + 2];
  }
}
