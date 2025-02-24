//
// This is only a SKELETON file for the 'Rational Numbers' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Rational {
  constructor(numerator, denominator) {
    const gcd = Rational.gcd(Math.abs(numerator), Math.abs(denominator));
    let num = numerator / gcd;
    let denom = denominator / gcd;
    if (denom < 0) {
      num *= -1;
      denom *= -1;
    }
    this.num = num;
    this.denom = denom;
  }

  static gcd(a, b) {
    while (b !== 0) {
      [a, b] = [b, a % b];
    }
    return a;
  }

  add(other) {
    const numerator = this.num * other.denom + other.num * this.denom;
    const denominator = this.denom * other.denom;
    return new Rational(numerator, denominator);
  }

  sub(other) {
    const numerator = this.num * other.denom - other.num * this.denom;
    const denominator = this.denom * other.denom;
    return new Rational(numerator, denominator);
  }

  mul(other) {
    const numerator = this.num * other.num;
    const denominator = this.denom * other.denom;
    return new Rational(numerator, denominator);
  }

  div(other) {
    const numerator = this.num * other.denom;
    const denominator = this.denom * other.num;
    return new Rational(numerator, denominator);
  }

  abs() {
    return new Rational(Math.abs(this.num), Math.abs(this.denom));
  }

  exprational(n) {
    if (n >= 0) {
      return new Rational(Math.pow(this.num, n), Math.pow(this.denom, n));
    } else {
      const m = -n;
      return new Rational(Math.pow(this.denom, m), Math.pow(this.num, m));
    }
  }

  expreal(x) {
    // Calculate root first, then power to maintain precision with integer exponents
    return Math.pow(Math.pow(x, 1/this.denom), this.num);
  }

  reduce() {
    return new Rational(this.num, this.denom);
  }
}
