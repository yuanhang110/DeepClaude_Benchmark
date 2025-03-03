//
// This is only a SKELETON file for the 'Triangle' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Triangle {
  constructor(...sides) {
    this.sides = sides;
    const [a, b, c] = sides;
    this.isValid = sides.length === 3 && sides.every(s => s > 0) &&
      a + b >= c &&
      b + c >= a &&
      a + c >= b;
  }

  get isEquilateral() {
    return this.isValid && this.sides.every(s => s === this.sides[0]);
  }

  get isIsosceles() {
    return this.isValid && (
      this.sides[0] === this.sides[1] ||
      this.sides[1] === this.sides[2] ||
      this.sides[0] === this.sides[2]
    );
  }

  get isScalene() {
    return this.isValid && new Set(this.sides).size === 3;
  }
}
