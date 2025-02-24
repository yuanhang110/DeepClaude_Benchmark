//
// This is only a SKELETON file for the 'Binary' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Binary {
  constructor(binary) {
    this.binary = binary;
  }

  toDecimal() {
    if (!this.binary || !/^[01]+$/.test(this.binary)) {
      return null;
    }

    let decimal = 0;
    const length = this.binary.length;
    for (let i = 0; i < length; i++) {
      const digit = parseInt(this.binary[i], 10);
      const exponent = length - 1 - i;
      decimal += digit * (2 ** exponent);
    }
    return decimal;
  }
}
