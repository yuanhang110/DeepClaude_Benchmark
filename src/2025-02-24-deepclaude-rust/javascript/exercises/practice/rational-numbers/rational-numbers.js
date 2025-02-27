//
// This is only a SKELETON file for the 'Rational Numbers' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

function computeGCD(a, b) {
  while (b !== 0) {
    const temp = b;
    b = a % b;
    a = temp;
  }
  return a;
}

export class Rational {
  constructor(numerator, denominator) {
    if (denominator === 0) {
      throw new Error("Denominator cannot be zero");
    }
    const gcd = computeGCD(Math.abs(numerator), Math.abs(denominator));
    let reducedNum = numerator / gcd;
    let reducedDen = denominator / gcd;

    if (reducedDen < 0) {
      reducedNum *= -1;
      reducedDen *= -1;
    }

    this.numerator = reducedNum;
    this.denominator = reducedDen;
  }

  add(other) {
    const newNumerator = this.numerator * other.denominator + other.numerator * this.denominator;
    const newDenominator = this.denominator * other.denominator;
    return new Rational(newNumerator, newDenominator);
  }

  sub(other) {
    const newNumerator = this.numerator * other.denominator - other.numerator * this.denominator;
    const newDenominator = this.denominator * other.denominator;
    return new Rational(newNumerator, newDenominator);
  }

  mul(other) {
    const newNumerator = this.numerator * other.numerator;
    const newDenominator = this.denominator * other.denominator;
    return new Rational(newNumerator, newDenominator);
  }

  div(other) {
    if (other.numerator === 0) {
      throw new Error("Division by zero");
    }
    const newNumerator = this.numerator * other.denominator;
    const newDenominator = this.denominator * other.numerator;
    return new Rational(newNumerator, newDenominator);
  }

  abs() {
    return new Rational(Math.abs(this.numerator), this.denominator);
  }

  exprational(n) {
    if (n >= 0) {
      const newNumerator = Math.pow(this.numerator, n);
      const newDenominator = Math.pow(this.denominator, n);
      return new Rational(newNumerator, newDenominator);
    } else {
      const m = -n;
      const newNumerator = Math.pow(this.denominator, m);
      const newDenominator = Math.pow(this.numerator, m);
      return new Rational(newNumerator, newDenominator);
    }
  }

  expreal(x) {
    return Math.pow(Math.pow(x, this.numerator), 1 / this.denominator);
  }

  reduce() {
    return new Rational(this.numerator, this.denominator);
  }
}
