//
// This is only a SKELETON file for the 'Binary' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Binary {
  constructor(binaryStr) {
    this.binaryStr = binaryStr;
  }

  toDecimal() {
    if (!this.binaryStr?.match(/^[01]+$/)) {
      return null;
    }

    return this.binaryStr.split('').reduce((acc, digit) => acc * 2 + parseInt(digit, 10), 0);
  }
}
