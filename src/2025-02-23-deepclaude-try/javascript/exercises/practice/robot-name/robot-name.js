// This is only a SKELETON file for the 'Robot Name' exercise. It's been
// provided as a convenience to get your started writing code faster.

export class Robot {
  static usedNames = new Set();

  constructor() {
    this.reset();
  }

  static generateName() {
    const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const numbers = '0123456789';
    let name;
    
    do {
      name = '';
      // Generate 2 random letters
      for (let i = 0; i < 2; i++) {
        name += letters[Math.floor(Math.random() * letters.length)];
      }
      // Generate 3 random numbers 
      for (let i = 0; i < 3; i++) {
        name += numbers[Math.floor(Math.random() * numbers.length)];
      }
    } while (Robot.usedNames.has(name));

    return name;
  }

  reset() {
    if (this._name) {
      Robot.usedNames.delete(this._name);
    }
    this._name = Robot.generateName();
    Robot.usedNames.add(this._name);
  }

  get name() {
    return this._name;
  }
}

Robot.releaseNames = () => {
  Robot.usedNames.clear();
};
