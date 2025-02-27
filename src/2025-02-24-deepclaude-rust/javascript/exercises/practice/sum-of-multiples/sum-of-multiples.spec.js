import { sum } from './sum-of-multiples';

describe('Sum Of Multiples', () => {
  test('no multiples within limit', () => {
    expect(sum([3, 5], 1)).toEqual(0);
  });

  test('one factor has multiples within limit', () => {
    expect(sum([3, 5], 4)).toEqual(3);
  });

  test('more than one multiple within limit', () => {
    expect(sum([3], 7)).toEqual(9);
  });

  test('more than one factor with multiples within limit', () => {
    expect(sum([3, 5], 10)).toEqual(23);
  });

  test('each multiple is only counted once', () => {
    expect(sum([3, 5], 100)).toEqual(2318);
  });

  test('a much larger limit', () => {
    expect(sum([3, 5], 1000)).toEqual(233168);
  });

  test('three factors', () => {
    expect(sum([7, 13, 17], 20)).toEqual(51);
  });

  test('factors not relatively prime', () => {
    expect(sum([4, 6], 15)).toEqual(30);
  });

  test('some pairs of factors relatively prime and some not', () => {
    expect(sum([5, 6, 8], 150)).toEqual(4419);
  });

  test('one factor is a multiple of another', () => {
    expect(sum([5, 25], 51)).toEqual(275);
  });

  test('much larger factors', () => {
    expect(sum([43, 47], 10000)).toEqual(2203160);
  });

  test('all numbers are multiples of 1', () => {
    expect(sum([1], 100)).toEqual(4950);
  });

  test('no factors means an empty sum', () => {
    expect(sum([], 10000)).toEqual(0);
  });

  test('the only multiple of 0 is 0', () => {
    expect(sum([0], 1)).toEqual(0);
  });

  test('the factor 0 does not affect the sum of multiples of other factors', () => {
    expect(sum([3, 0], 4)).toEqual(3);
  });

  test('solutions using include-exclude must extend to cardinality greater than 3', () => {
    expect(sum([2, 3, 5, 7, 11], 10000)).toEqual(39614537);
  });
});
