//
// This is only a SKELETON file for the 'Complex Numbers' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class ComplexNumber {
  constructor(a, b) {
    this.a = a;
    this.b = b;
    
    // Define conj as a non-enumerable property to prevent Jest from infinitely traversing it
    Object.defineProperty(this, 'conj', {
      get: function() {
        return new ComplexNumber(this.a, -this.b);
      },
      enumerable: false
    });
  }

  get real() {
    return this.a;
  }

  get imag() {
    return this.b;
  }

  add(other) {
    return new ComplexNumber(this.a + other.a, this.b + other.b);
  }

  sub(other) {
    return new ComplexNumber(this.a - other.a, this.b - other.b);
  }

  div(other) {
    const denom = other.a ** 2 + other.b ** 2;
    return new ComplexNumber(
      (this.a * other.a + this.b * other.b) / denom,
      (this.b * other.a - this.a * other.b) / denom
    );
  }

  mul(other) {
    return new ComplexNumber(
      this.a * other.a - this.b * other.b,
      this.b * other.a + this.a * other.b
    );
  }

  get abs() {
    return Math.sqrt(this.a ** 2 + this.b ** 2);
  }


  get exp() {
    const ea = Math.exp(this.a);
    return new ComplexNumber(
      ea * Math.cos(this.b),
      ea * Math.sin(this.b)
    );
  }
}
