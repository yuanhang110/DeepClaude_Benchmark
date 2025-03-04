//
// This is only a SKELETON file for the 'Resistor Color Trio' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class ResistorColorTrio {
  static COLORS = {
    'black': 0,
    'brown': 1,
    'red': 2,
    'orange': 3,
    'yellow': 4,
    'green': 5,
    'blue': 6,
    'violet': 7,
    'grey': 8,
    'white': 9
  };

  constructor(colors) {
    if (colors.length !== 3 || !colors.every(c => ResistorColorTrio.COLORS.hasOwnProperty(c))) {
      throw new Error('invalid color');
    }
    this.colors = colors;
  }

  label() {
    const [color1, color2, color3] = this.colors;
    const digit1 = ResistorColorTrio.COLORS[color1];
    const digit2 = ResistorColorTrio.COLORS[color2];
    const exponent = ResistorColorTrio.COLORS[color3];
    
    const mainValue = digit1 * 10 + digit2;
    const totalOhms = mainValue * (10 ** exponent);
    
    let value = totalOhms;
    let unit = 'ohms';
    
    const prefixes = [
      { threshold: 1e9, prefix: 'giga' },
      { threshold: 1e6, prefix: 'mega' },
      { threshold: 1e3, prefix: 'kilo' }
    ];
    
    for (const { threshold, prefix } of prefixes) {
      if (totalOhms >= threshold) {
        value = totalOhms / threshold;
        unit = `${prefix}ohms`;
        break;
      }
    }
    
    let formattedValue;
    if (Number.isInteger(value)) {
      formattedValue = value.toString();
    } else {
      formattedValue = value.toFixed(1);
    }
    
    return `${formattedValue} ${unit}`;
  }
}
