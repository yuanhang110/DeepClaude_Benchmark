export class Triangle {
  constructor(...sides) {
    this.sides = sides;
  }

  isValidTriangle() {
    const [a, b, c] = this.sides;
    // All sides must be positive
    if (a <= 0 || b <= 0 || c <= 0) return false;
    // Triangle inequality theorem
    return (a + b >= c) && (b + c >= a) && (a + c >= b);
  }

  get isEquilateral() {
    if (!this.isValidTriangle()) return false;
    const [a, b, c] = this.sides;
    return a === b && b === c;
  }

  get isIsosceles() {
    if (!this.isValidTriangle()) return false;
    const [a, b, c] = this.sides;
    return a === b || b === c || a === c;
  }

  get isScalene() {
    if (!this.isValidTriangle()) return false;
    const [a, b, c] = this.sides;
    return a !== b && b !== c && a !== c;
  }
}
