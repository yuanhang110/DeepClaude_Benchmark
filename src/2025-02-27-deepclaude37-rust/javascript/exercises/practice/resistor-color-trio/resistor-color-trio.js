//
// This is only a SKELETON file for the 'Resistor Color Trio' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class ResistorColorTrio {
  static COLOR_VALUES = ['black', 'brown', 'red', 'orange', 'yellow', 'green', 'blue', 'violet', 'grey', 'white'];

  constructor(colors) {
    this.colors = colors;
  }

  get label() {
    const firstValue = ResistorColorTrio.COLOR_VALUES.indexOf(this.colors[0]);
    const secondValue = ResistorColorTrio.COLOR_VALUES.indexOf(this.colors[1]);
    const exponent = ResistorColorTrio.COLOR_VALUES.indexOf(this.colors[2]);
    
    if (firstValue === -1 || secondValue === -1 || exponent === -1) {
      throw new Error('invalid color');
    }
    
    let value = (firstValue * 10 + secondValue) * Math.pow(10, exponent);
    
    const prefixes = [
      { multiplier: 1e9, name: 'giga' },
      { multiplier: 1e6, name: 'mega' },
      { multiplier: 1e3, name: 'kilo' },
    ];
    
    let suffix = 'ohms';
    for (const { multiplier, name } of prefixes) {
      if (value >= multiplier && value % multiplier === 0) {
        value = value / multiplier;
        suffix = `${name}${suffix}`;
        break;
      }
    }
    
    return `Resistor value: ${value} ${suffix}`;
  }
}
