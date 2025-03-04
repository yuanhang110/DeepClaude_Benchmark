export class Robot {
  static usedNames = new Set();

  static releaseNames(name) {
    Robot.usedNames.delete(name);
  }

  constructor() {
    this._name = null;
  }

  get name() {
    if (!this._name) {
      do {
        this._name = this._generateName();
      } while (Robot.usedNames.has(this._name));
      Robot.usedNames.add(this._name);
    }
    return this._name;
  }

  set name(value) {
    throw new Error('Cannot modify robot name');
  }

  reset() {
    if (this._name) {
      Robot.releaseNames(this._name);
      this._name = null;
    }
  }

  _generateName() {
    const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const digits = '0123456789';
    let name = '';
    for (let i = 0; i < 2; i++) {
      name += letters[Math.floor(Math.random() * 26)];
    }
    for (let i = 0; i < 3; i++) {
      name += digits[Math.floor(Math.random() * 10)];
    }
    return name;
  }
}
