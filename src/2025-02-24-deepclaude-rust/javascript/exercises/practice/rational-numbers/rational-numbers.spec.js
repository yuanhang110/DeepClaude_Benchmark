import { Rational } from './rational-numbers';

describe('Addition', () => {
  test('Add two positive rational numbers', () => {
    const expected = new Rational(7, 6);
    expect(new Rational(1, 2).add(new Rational(2, 3))).toEqual(expected);
  });

  test('Add a positive rational number and a negative rational number', () => {
    const expected = new Rational(-1, 6);
    expect(new Rational(1, 2).add(new Rational(-2, 3))).toEqual(expected);
  });

  test('Add two negative rational numbers', () => {
    const expected = new Rational(-7, 6);
    expect(new Rational(-1, 2).add(new Rational(-2, 3))).toEqual(expected);
  });

  test('Add a rational number to its additive inverse', () => {
    const expected = new Rational(0, 1);
    expect(new Rational(1, 2).add(new Rational(-1, 2))).toEqual(expected);
  });
});

describe('Subtraction', () => {
  test('Subtract two positive rational numbers', () => {
    const expected = new Rational(-1, 6);
    expect(new Rational(1, 2).sub(new Rational(2, 3))).toEqual(expected);
  });

  test('Subtract a positive rational number and a negative rational number', () => {
    const expected = new Rational(7, 6);
    expect(new Rational(1, 2).sub(new Rational(-2, 3))).toEqual(expected);
  });

  test('Subtract two negative rational numbers', () => {
    const expected = new Rational(1, 6);
    expect(new Rational(-1, 2).sub(new Rational(-2, 3))).toEqual(expected);
  });

  test('Subtract a rational number from itself', () => {
    const expected = new Rational(0, 1);
    expect(new Rational(1, 2).sub(new Rational(1, 2))).toEqual(expected);
  });
});

describe('Multiplication', () => {
  test('Multiply two positive rational numbers', () => {
    const expected = new Rational(1, 3);
    expect(new Rational(1, 2).mul(new Rational(2, 3))).toEqual(expected);
  });

  test('Multiply a negative rational number by a positive rational number', () => {
    const expected = new Rational(-1, 3);
    expect(new Rational(-1, 2).mul(new Rational(2, 3))).toEqual(expected);
  });

  test('Multiply two negative rational numbers', () => {
    const expected = new Rational(1, 3);
    expect(new Rational(-1, 2).mul(new Rational(-2, 3))).toEqual(expected);
  });

  test('Multiply a rational number by its reciprocal', () => {
    const expected = new Rational(1, 1);
    expect(new Rational(1, 2).mul(new Rational(2, 1))).toEqual(expected);
  });

  test('Multiply a rational number by 1', () => {
    const expected = new Rational(1, 2);
    expect(new Rational(1, 2).mul(new Rational(1, 1))).toEqual(expected);
  });

  test('Multiply a rational number by 0', () => {
    const expected = new Rational(0, 1);
    expect(new Rational(1, 2).mul(new Rational(0, 1))).toEqual(expected);
  });
});

describe('Division', () => {
  test('Divide two positive rational numbers', () => {
    const expected = new Rational(3, 4);
    expect(new Rational(1, 2).div(new Rational(2, 3))).toEqual(expected);
  });

  test('Divide a positive rational number by a negative rational number', () => {
    const expected = new Rational(-3, 4);
    expect(new Rational(1, 2).div(new Rational(-2, 3))).toEqual(expected);
  });

  test('Divide two negative rational numbers', () => {
    const expected = new Rational(3, 4);
    expect(new Rational(-1, 2).div(new Rational(-2, 3))).toEqual(expected);
  });

  test('Divide a rational number by 1', () => {
    const expected = new Rational(1, 2);
    expect(new Rational(1, 2).div(new Rational(1, 1))).toEqual(expected);
  });
});

describe('Absolute value', () => {
  test('Absolute value of a positive rational number', () => {
    const expected = new Rational(1, 2);
    expect(new Rational(1, 2).abs()).toEqual(expected);
  });

  test('Absolute value of a negative rational number', () => {
    const expected = new Rational(1, 2);
    expect(new Rational(-1, 2).abs()).toEqual(expected);
  });

  test('Absolute value of zero', () => {
    const expected = new Rational(0, 1);
    expect(new Rational(0, 1).abs()).toEqual(expected);
  });
});

describe('Exponentiation of a rational number', () => {
  test('Raise a positive rational number to a positive integer power', () => {
    const expected = new Rational(1, 8);
    expect(new Rational(1, 2).exprational(3)).toEqual(expected);
  });

  test('Raise a negative rational number to a positive integer power', () => {
    const expected = new Rational(-1, 8);
    expect(new Rational(-1, 2).exprational(3)).toEqual(expected);
  });

  test('Raise zero to an integer power', () => {
    const expected = new Rational(0, 1);
    expect(new Rational(0, 1).exprational(5)).toEqual(expected);
  });

  test('Raise one to an integer power', () => {
    const expected = new Rational(1, 1);
    expect(new Rational(1, 1).exprational(4)).toEqual(expected);
  });

  test('Raise a positive rational number to the power of zero', () => {
    const expected = new Rational(1, 1);
    expect(new Rational(1, 2).exprational(0)).toEqual(expected);
  });

  test('Raise a negative rational number to the power of zero', () => {
    const expected = new Rational(1, 1);
    expect(new Rational(-1, 2).exprational(0)).toEqual(expected);
  });
});

describe('Exponentiation of a real number to a rational number', () => {
  test('Raise a real number to a positive rational number', () => {
    const expected = 16.0;
    expect(new Rational(4, 3).expreal(8)).toEqual(expected);
  });

  test('Raise a real number to a negative rational number', () => {
    expect(new Rational(-1, 2).expreal(9)).toBeCloseTo(0.33, 2);
  });

  test('Raise a real number to a zero rational number', () => {
    const expected = 1.0;
    expect(new Rational(0, 1).expreal(2)).toEqual(expected);
  });
});

describe('Reduction to lowest terms', () => {
  test('Reduce a positive rational number to lowest terms', () => {
    const expected = new Rational(1, 2);
    expect(new Rational(2, 4).reduce()).toEqual(expected);
  });

  test('Reduce a negative rational number to lowest terms', () => {
    const expected = new Rational(-2, 3);
    expect(new Rational(-4, 6).reduce()).toEqual(expected);
  });

  test('Reduce a rational number with a negative denominator to lowest terms', () => {
    const expected = new Rational(-1, 3);
    expect(new Rational(3, -9).reduce()).toEqual(expected);
  });

  test('Reduce zero to lowest terms', () => {
    const expected = new Rational(0, 1);
    expect(new Rational(0, 6).reduce()).toEqual(expected);
  });

  test('Reduce an integer to lowest terms', () => {
    const expected = new Rational(-2, 1);
    expect(new Rational(-14, 7).reduce()).toEqual(expected);
  });

  test('Reduce one to lowest terms', () => {
    const expected = new Rational(1, 1);
    expect(new Rational(13, 13).reduce()).toEqual(expected);
  });
});
