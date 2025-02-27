//
// This is only a SKELETON file for the 'Resistor Color Trio' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class ResistorColorTrio {
  constructor(colors) {
    this.colors = colors;
  }

  get label() {
    const colorCodes = {
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

    const [first, second, third] = this.colors;
    const firstVal = colorCodes[first];
    const secondVal = colorCodes[second];
    const exponent = colorCodes[third];

    if (firstVal === undefined || secondVal === undefined || exponent === undefined) {
      throw new Error('invalid color');
    }

    const mainValue = firstVal * 10 + secondVal;
    const totalOhms = mainValue * (10 ** exponent);

    const divisors = [
      { divisor: 1e9, prefix: 'giga' },
      { divisor: 1e6, prefix: 'mega' },
      { divisor: 1e3, prefix: 'kilo' },
    ];

    for (const { divisor, prefix } of divisors) {
      if (totalOhms >= divisor && totalOhms % divisor === 0) {
        const value = totalOhms / divisor;
        return `Resistor value: ${value} ${prefix}ohms`;
      }
    }

    return `Resistor value: ${totalOhms} ohms`;
  }
}
