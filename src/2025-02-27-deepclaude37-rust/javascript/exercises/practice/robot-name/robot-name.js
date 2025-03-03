// This is only a SKELETON file for the 'Robot Name' exercise. It's been
// provided as a convenience to get your started writing code faster.

export class Robot {
  static usedNames = new Set();
  #currentName = null;

  get name() {
    if (!this.#currentName) {
      let newName;
      do {
        newName = Robot.generateRandomName();
      } while (Robot.usedNames.has(newName));
      Robot.usedNames.add(newName);
      this.#currentName = newName;
    }
    return this.#currentName;
  }

  reset() {
    // Don't remove the old name from usedNames
    // This ensures all names ever generated remain unique
    this.#currentName = null;
  }

  static generateRandomName() {
    const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const digits = '0123456789';
    
    let name = '';
    // Add two random uppercase letters
    for (let i = 0; i < 2; i++) {
      name += letters.charAt(Math.floor(Math.random() * letters.length));
    }
    
    // Add three random digits
    for (let i = 0; i < 3; i++) {
      name += digits.charAt(Math.floor(Math.random() * digits.length));
    }
    
    return name;
  }
}

Robot.releaseNames = () => {
  Robot.usedNames.clear();
};
