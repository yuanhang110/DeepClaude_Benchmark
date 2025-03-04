const COLOR_CODES = {
  black: 0,
  brown: 1,
  red: 2,
  orange: 3,
  yellow: 4,
  green: 5,
  blue: 6,
  violet: 7,
  grey: 8,
  white: 9
};

export class ResistorColorTrio {
  constructor(colors) {
    this.colors = colors;
  }

  label() {
    // Validate input
    if (!this.colors || this.colors.length !== 3) {
      throw new Error('Exactly 3 colors are required');
    }

    // Get values for each band
    const [first, second, zeros] = this.colors.map(color => {
      const value = COLOR_CODES[color.toLowerCase()];
      if (value === undefined) {
        throw new Error(`invalid color`);
      }
      return value;
    });

    // Calculate resistance value
    const mainValue = first * 10 + second;
    let resistance = mainValue * Math.pow(10, zeros);

    // Determine the appropriate metric prefix
    let unit = 'ohms';
    if (resistance >= 1000000) {
      resistance /= 1000000;
      unit = 'megaohms';
    } else if (resistance >= 1000) {
      resistance /= 1000;
      unit = 'kiloohms';
    }

    // Format the output to match expected format
    return `Resistor value: ${resistance} ${unit}`;
  }
}
