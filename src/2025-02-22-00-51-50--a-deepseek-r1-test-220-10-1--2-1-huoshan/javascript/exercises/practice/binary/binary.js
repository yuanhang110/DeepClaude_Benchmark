//
// This is only a SKELETON file for the 'Binary' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Binary {
  constructor(binary) {
    this.binary = binary.trim();
  }

  toDecimal() {
    let decimal = 0;
    const length = this.binary.length;
    
    if (length === 0) {
      return null;
    }
    
    for (let i = 0; i < length; i++) {
      const char = this.binary[i];
      if (char !== '0' && char !== '1') {
        return null;
      }
      decimal += parseInt(char) * Math.pow(2, length - i - 1);
    }
    
    return decimal;
  }
}
