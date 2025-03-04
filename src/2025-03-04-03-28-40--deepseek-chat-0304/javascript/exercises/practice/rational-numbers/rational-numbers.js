// Helper function to calculate greatest common divisor
function gcd(a, b) {
  if (!b) return Math.abs(a);
  return gcd(b, a % b);
}

export class Rational {
  constructor(a, b) {
    if (b === 0) throw new Error('Denominator cannot be zero');
    this.a = a;
    this.b = b;
    this.reduce();
  }

  add(other) {
    const numerator = this.a * other.b + other.a * this.b;
    const denominator = this.b * other.b;
    return new Rational(numerator, denominator);
  }

  sub(other) {
    const numerator = this.a * other.b - other.a * this.b;
    const denominator = this.b * other.b;
    return new Rational(numerator, denominator);
  }

  mul(other) {
    const numerator = this.a * other.a;
    const denominator = this.b * other.b;
    return new Rational(numerator, denominator);
  }

  div(other) {
    if (other.a === 0) throw new Error('Cannot divide by zero');
    const numerator = this.a * other.b;
    const denominator = this.b * other.a;
    return new Rational(numerator, denominator);
  }

  abs() {
    return new Rational(Math.abs(this.a), Math.abs(this.b));
  }

  exprational(n) {
    if (n >= 0) {
      return new Rational(Math.pow(this.a, n), Math.pow(this.b, n));
    } else {
      const m = Math.abs(n);
      return new Rational(Math.pow(this.b, m), Math.pow(this.a, m));
    }
  }

  expreal(x) {
    // Calculate x^a first, then take the b-th root
    const numeratorPower = Math.pow(x, this.a);
    // Use Math.pow for the root since ES6 doesn't have a root function
    return Math.pow(numeratorPower, 1 / this.b);
  }

  reduce() {
    const divisor = gcd(this.a, this.b);
    this.a = this.a / divisor;
    this.b = this.b / divisor;
    
    // Ensure denominator is positive
    if (this.b < 0) {
      this.a *= -1;
      this.b *= -1;
    }
    
    return this;
  }
  
  toString() {
    return `${this.a}/${this.b}`;
  }
}
