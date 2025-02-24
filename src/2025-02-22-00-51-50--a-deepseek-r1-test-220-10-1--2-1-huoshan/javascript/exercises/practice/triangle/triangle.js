//
// This is only a SKELETON file for the 'Triangle' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Triangle {
  constructor(...sides) {
    this.sides = sides.sort((a, b) => a - b);
    if (this.sides.some(side => side <= 0) || 
        this.sides[0] + this.sides[1] <= this.sides[2] ||
        this.sides[0] + this.sides[2] <= this.sides[1] ||
        this.sides[1] + this.sides[2] <= this.sides[0]) {
      throw new Error('Invalid triangle');
    }
  }

  get isEquilateral() {
    return this.sides[0] === this.sides[1] && this.sides[1] === this.sides[2];
  }

  get isIsosceles() {
    return this.sides[0] === this.sides[1] || this.sides[1] === this.sides[2];
  }

  get isScalene() {
    return this.sides[0] !== this.sides[1] && this.sides[1] !== this.sides[2];
  }
}
