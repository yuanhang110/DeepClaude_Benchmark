export class Binary {
  constructor(binaryStr) {
    this.binaryStr = binaryStr;
  }

  toDecimal() {
    // Handle invalid input
    if (!this.binaryStr || !/^[01]+$/.test(this.binaryStr)) {
      return null;
    }

    let decimal = 0;
    const length = this.binaryStr.length;
    
    // Convert each binary digit to decimal
    for (let i = 0; i < length; i++) {
      const digit = parseInt(this.binaryStr[length - 1 - i]);
      decimal += digit * Math.pow(2, i);
    }
    
    return decimal;
  }
}
