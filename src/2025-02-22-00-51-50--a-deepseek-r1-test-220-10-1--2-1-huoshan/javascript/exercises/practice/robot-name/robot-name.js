// This is only a SKELETON file for the 'Robot Name' exercise. It's been
// provided as a convenience to get your started writing code faster.

export class Robot {
  static usedNames = new Set();

  constructor() {
    this._name = null;
  }

  get name() {
    if (!this._name) {
      let newName;
      do {
        const letters = String.fromCharCode(
          65 + Math.floor(Math.random() * 26),
          65 + Math.floor(Math.random() * 26)
        );
        const digits = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
        newName = letters + digits;
      } while (Robot.usedNames.has(newName));
      
      Robot.usedNames.add(newName);
      this._name = newName;
    }
    return this._name;
  }

  reset() {
    if (this._name) {
      this._name = null;
    }
  }
}

Robot.releaseNames = () => {};
