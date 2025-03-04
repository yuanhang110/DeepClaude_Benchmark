import { say } from './say';

describe('say', () => {
  test('zero', () => {
    expect(say(0)).toBe('zero');
  });

  test('one', () => {
    expect(say(1)).toBe('one');
  });

  test('fourteen', () => {
    expect(say(14)).toBe('fourteen');
  });

  test('twenty', () => {
    expect(say(20)).toBe('twenty');
  });

  test('twenty-two', () => {
    expect(say(22)).toBe('twenty-two');
  });

  test('one hundred', () => {
    expect(say(100)).toBe('one hundred');
  });

  test('one hundred twenty-three', () => {
    expect(say(123)).toBe('one hundred twenty-three');
  });

  test('one thousand', () => {
    expect(say(1000)).toBe('one thousand');
  });

  test('one thousand two hundred thirty-four', () => {
    expect(say(1234)).toBe('one thousand two hundred thirty-four');
  });

  test('one million', () => {
    expect(say(1000000)).toBe('one million');
  });

  test('one million two', () => {
    expect(say(1000002)).toBe('one million two');
  });

  test('one million two thousand three hundred forty-five', () => {
    expect(say(1002345)).toBe(
      'one million two thousand three hundred forty-five',
    );
  });

  test('one billion', () => {
    expect(say(1000000000)).toBe('one billion');
  });

  test('a really big number', () => {
    let expected = 'nine hundred eighty-seven billion ';
    expected += 'six hundred fifty-four million ';
    expected += 'three hundred twenty-one thousand ';
    expected += 'one hundred twenty-three';
    expect(say(987654321123)).toBe(expected);
  });

  test('raises an error below zero', () => {
    expect(() => {
      say(-1);
    }).toThrow(new Error('Number must be between 0 and 999,999,999,999.'));
  });

  test('raises an error above 999,999,999,999', () => {
    expect(() => {
      say(1000000000000);
    }).toThrow(new Error('Number must be between 0 and 999,999,999,999.'));
  });
});
