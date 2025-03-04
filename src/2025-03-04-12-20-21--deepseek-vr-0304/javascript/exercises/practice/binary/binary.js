//
// This is only a SKELETON file for the 'Binary' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Binary {
  constructor(binaryString) {
    this.binary = binaryString;
  }

  toDecimal() {
    if (!this.binary.match(/^[01]+$/)) return null;
    return this.binary.split('').reverse().reduce((sum, digit, index) => {
      return sum + parseInt(digit) * Math.pow(2, index);
    }, 0);
  }
}
