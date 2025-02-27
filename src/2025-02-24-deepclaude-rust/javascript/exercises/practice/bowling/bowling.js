//
// This is only a SKELETON file for the 'Bowling' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Bowling {
  constructor() {
    this.rolls = [];
  }

  roll(pins) {
    if (pins < 0) {
      throw new Error('Negative roll is invalid');
    }
    if (pins > 10) {
      throw new Error('Pin count exceeds pins on the lane');
    }

    if (this.isGameComplete()) {
      throw new Error('Cannot roll after game is over');
    }

    this.validateRoll(pins);
    this.rolls.push(pins);
  }

  score() {
    if (!this.isGameComplete()) {
      throw new Error('Score cannot be taken until the end of the game');
    }

    let total = 0;
    let rollIndex = 0;

    for (let frame = 0; frame < 10; frame++) {
      if (this.isStrike(rollIndex)) {
        total += 10 + this.strikeBonus(rollIndex);
        rollIndex += 1;
      } else if (this.isSpare(rollIndex)) {
        total += 10 + this.spareBonus(rollIndex);
        rollIndex += 2;
      } else {
        total += this.sumFrame(rollIndex);
        rollIndex += 2;
      }
    }

    return total;
  }

  isStrike(rollIndex) {
    return this.rolls[rollIndex] === 10;
  }

  isSpare(rollIndex) {
    return this.rolls[rollIndex] + this.rolls[rollIndex + 1] === 10;
  }

  strikeBonus(rollIndex) {
    return this.rolls[rollIndex + 1] + this.rolls[rollIndex + 2];
  }

  spareBonus(rollIndex) {
    return this.rolls[rollIndex + 2];
  }

  sumFrame(rollIndex) {
    return this.rolls[rollIndex] + this.rolls[rollIndex + 1];
  }

  isGameComplete() {
    let frameCount = 0;
    let i = 0;

    while (i < this.rolls.length && frameCount < 9) {
      if (this.rolls[i] === 10) {
        frameCount++;
        i++;
      } else {
        if (i + 1 < this.rolls.length) {
          frameCount++;
          i += 2;
        } else {
          return false;
        }
      }
    }

    if (frameCount < 10) return false;

    const tenthFrameStart = i;
    const tenthFrameRolls = this.rolls.slice(tenthFrameStart);
    const tenthFrameLength = tenthFrameRolls.length;

    if (tenthFrameLength === 0) return false;

    const firstRoll = tenthFrameRolls[0];

    if (firstRoll === 10) {
      return tenthFrameLength >= 3;
    } else if (tenthFrameLength >= 2) {
      const secondRoll = tenthFrameRolls[1];
      if (firstRoll + secondRoll === 10) {
        return tenthFrameLength >= 3;
      } else {
        return tenthFrameLength >= 2;
      }
    }

    return false;
  }

  validateRoll(pins) {
    const { frame, position } = this.getCurrentFrameAndPosition();

    if (frame < 10) {
      if (position === 1) {
        const previous = this.rolls[this.rolls.length - 1];
        if (previous + pins > 10) {
          throw new Error('Pin count exceeds pins on the lane');
        }
      }
    } else {
      const tenthFrameRolls = this.rolls.slice(this.getTenthFrameStartIndex());
      const len = tenthFrameRolls.length;

      if (len === 1) {
        const first = tenthFrameRolls[0];
        if (first !== 10 && first + pins > 10) {
          throw new Error('Invalid frame score (>10)');
        }
      } else if (len === 2) {
        const first = tenthFrameRolls[0];
        const second = tenthFrameRolls[1];
        if (first === 10) {
          if (second !== 10 && second + pins > 10) {
            throw new Error('Invalid frame score (>10)');
          }
        } else if (first + second !== 10) {
          throw new Error('Cannot have third roll in open 10th frame');
        }
      }
    }
  }

  getCurrentFrameAndPosition() {
    let frame = 1;
    let position = 0;
    let i = 0;

    while (i < this.rolls.length && frame < 10) {
      if (this.rolls[i] === 10) {
        frame++;
        i++;
        position = 0;
      } else {
        if (i + 1 < this.rolls.length) {
          frame++;
          i += 2;
          position = 0;
        } else {
          position = this.rolls.length - i;
          return { frame, position };
        }
      }
    }

    if (frame < 10) {
      position = this.rolls.length - i;
      return { frame, position };
    } else {
      position = this.rolls.length - this.getTenthFrameStartIndex();
      return { frame: 10, position };
    }
  }

  getTenthFrameStartIndex() {
    let frameCount = 0;
    let i = 0;

    while (i < this.rolls.length && frameCount < 10) {
      if (this.rolls[i] === 10) {
        frameCount++;
        i++;
      } else {
        if (i + 1 < this.rolls.length) {
          frameCount++;
          i += 2;
        } else {
          break;
        }
      }
    }

    return i;
  }
}
