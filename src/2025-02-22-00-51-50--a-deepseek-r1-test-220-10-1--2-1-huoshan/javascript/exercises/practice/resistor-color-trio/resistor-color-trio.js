//
// This is only a SKELETON file for the 'Resistor Color Trio' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class ResistorColorTrio {
  static COLOR_VALUES = {
    black: 0,
    brown: 1,
    red: 2,
    orange: 3,
    yellow: 4,
    green: 5,
    blue: 6,
    violet: 7,
    grey: 8,
    white: 9,
  };

  constructor(first, second, third) {
    [first, second, third].forEach(color => {
      if (!Object.hasOwnProperty.call(ResistorColorTrio.COLOR_VALUES, color)) {
        throw new Error('invalid color');
      }
    });
    this.first = first;
    this.second = second;
    this.third = third;
  }

  label() {
    const colorValues = ResistorColorTrio.COLOR_VALUES;
    const firstVal = colorValues[this.first];
    const secondVal = colorValues[this.second];
    const exponent = colorValues[this.third];
    
    let value = (firstVal * 10 + secondVal) * (10 ** exponent);
    let divisor = 1;
    let prefix = '';
    
    if (value >= 1e9 && value % 1e9 === 0) {
      divisor = 1e9;
      prefix = 'giga';
    } else if (value >= 1e6 && value % 1e6 === 0) {
      divisor = 1e6;
      prefix = 'mega';
    } else if (value >= 1e3 && value % 1e3 === 0) {
      divisor = 1e3;
      prefix = 'kilo';
    }
    
    value = value / divisor;
    
    // Handle plural for ohms when there's no prefix
    const unit = prefix ? `${prefix}ohms` : 'ohms';
    return `${value} ${unit}`;
  }
}
