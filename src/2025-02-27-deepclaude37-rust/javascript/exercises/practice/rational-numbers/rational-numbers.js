//
// This is only a SKELETON file for the 'Rational Numbers' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Rational {
  constructor(numerator, denominator) {
    if (denominator === 0) {
      throw new Error("Denominator cannot be zero");
    }
    
    // Store initial values
    this.numerator = numerator;
    this.denominator = denominator;
    
    // Reduce to lowest terms
    this.reduce();
  }

  static gcd(a, b) {
    a = Math.abs(a);
    b = Math.abs(b);
    
    // Euclidean algorithm
    while (b !== 0) {
      const temp = b;
      b = a % b;
      a = temp;
    }
    
    return a;
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
      // Positive exponent: (a/b)^n = (a^n)/(b^n)
      return new Rational(Math.pow(this.numerator, n), Math.pow(this.denominator, n));
    } else {
      // Negative exponent: (a/b)^(-n) = (b^n)/(a^n)
      const m = Math.abs(n);
      return new Rational(Math.pow(this.denominator, m), Math.pow(this.numerator, m));
    }
  }

  expreal(x) {
    // x^(a/b) = root(x^a, b)
    // To avoid floating point precision issues, calculate as (x^a)^(1/b)
    return Math.pow(Math.pow(x, this.numerator), 1 / this.denominator);
  }

  reduce() {
    // Find the greatest common divisor
    const gcd = Rational.gcd(this.numerator, this.denominator);
    
    // Divide both numerator and denominator by gcd
    this.numerator = this.numerator / gcd;
    this.denominator = this.denominator / gcd;
    
    // Ensure the denominator is positive
    if (this.denominator < 0) {
      this.numerator = -this.numerator;
      this.denominator = -this.denominator;
    }
    
    return this;
  }
}
