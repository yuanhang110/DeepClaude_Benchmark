export class Robot {
  constructor() {
    this._name = null;
    this.generateName();
  }

  get name() {
    return this._name;
  }

  generateName() {
    let newName;
    do {
      // Generate two random letters (A-Z)
      const letters = String.fromCharCode(
        65 + Math.floor(Math.random() * 26),
        65 + Math.floor(Math.random() * 26)
      );
      // Generate three random digits
      const numbers = Math.floor(Math.random() * 1000)
        .toString()
        .padStart(3, '0');
      newName = letters + numbers;
    } while (Robot.usedNames.has(newName));
    
    this._name = newName;
    Robot.usedNames.add(newName);
  }

  reset() {
    const oldName = this._name;
    this.generateName();
    Robot.usedNames.delete(oldName);
  }
}

// Static property to track all used names
Robot.usedNames = new Set();

Robot.releaseNames = () => {
  Robot.usedNames.clear();
};
