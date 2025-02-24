//
// This is only a SKELETON file for the 'Resistor Color Trio' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class ResistorColorTrio {
  constructor(colors) {
    this.colors = colors;
  }

  get label() {
    const colorMap = {
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

    if (!colorMap.hasOwnProperty(first) || !colorMap.hasOwnProperty(second) || !colorMap.hasOwnProperty(third)) {
      throw new Error('invalid color');
    }

    const digit1 = colorMap[first];
    const digit2 = colorMap[second];
    const exponent = colorMap[third];

    const mainValue = digit1 * 10 + digit2;
    const totalOhms = mainValue * (10 ** exponent);

    let value = totalOhms;
    let prefix = '';

    if (totalOhms >= 1e9 && totalOhms % 1e9 === 0) {
      value = totalOhms / 1e9;
      prefix = 'giga';
    } else if (totalOhms >= 1e6 && totalOhms % 1e6 === 0) {
      value = totalOhms / 1e6;
      prefix = 'mega';
    } else if (totalOhms >= 1e3 && totalOhms % 1e3 === 0) {
      value = totalOhms / 1e3;
      prefix = 'kilo';
    }

    return prefix ? `${value} ${prefix}ohms` : `${value} ohms`;
  }
}
