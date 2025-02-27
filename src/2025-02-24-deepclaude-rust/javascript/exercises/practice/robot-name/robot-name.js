// This is only a SKELETON file for the 'Robot Name' exercise. It's been
// provided as a convenience to get your started writing code faster.

export class Robot {
  constructor() {
    this._name = null;
  }

  reset() {
    this._name = null;
  }

  get name() {
    if (!this._name) {
      this._name = this.generateName();
    }
    return this._name;
  }

  generateName() {
    let name;
    do {
      const letters = String.fromCharCode(
        Math.floor(Math.random() * 26) + 65,
        Math.floor(Math.random() * 26) + 65
      );
      const digits = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
      name = letters + digits;
    } while (Robot.usedNames.has(name));

    Robot.usedNames.add(name);
    return name;
  }
}

Robot.usedNames = new Set();
Robot.releaseNames = () => {
  Robot.usedNames.clear();
};
