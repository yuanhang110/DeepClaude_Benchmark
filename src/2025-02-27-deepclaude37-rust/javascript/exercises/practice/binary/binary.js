//
// This is only a SKELETON file for the 'Binary' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Binary {
  constructor(binaryString) {
    this.binary = binaryString;
  }

  toDecimal() {
    // Handle invalid inputs (non-binary characters or empty string)
    if (!this.binary || !this.binary.match(/^[01]+$/)) {
      return null;
    }
    
    let decimal = 0;
    const length = this.binary.length;
    
    // Convert binary to decimal using first principles
    for (let i = 0; i < length; i++) {
      const bit = parseInt(this.binary[i], 10);
      const power = length - 1 - i;
      decimal += bit * Math.pow(2, power);
    }
    
    return decimal;
  }
}
