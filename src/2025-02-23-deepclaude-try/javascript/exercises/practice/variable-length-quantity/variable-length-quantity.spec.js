import { encode, decode } from './variable-length-quantity';

describe('VariableLengthQuantity', () => {
  describe('Encode a series of integers, producing a series of bytes.', () => {
    test('zero', () => {
      expect(encode([0])).toEqual([0]);
    });

    test('arbitrary single byte', () => {
      expect(encode([0x40])).toEqual([0x40]);
    });

    test('largest single byte', () => {
      expect(encode([0x7f])).toEqual([0x7f]);
    });

    test('smallest double byte', () => {
      expect(encode([0x80])).toEqual([0x81, 0]);
    });

    test('arbitrary double byte', () => {
      expect(encode([0x2000])).toEqual([0xc0, 0]);
    });

    test('largest double byte', () => {
      expect(encode([0x3fff])).toEqual([0xff, 0x7f]);
    });

    test('smallest triple byte', () => {
      expect(encode([0x4000])).toEqual([0x81, 0x80, 0]);
    });

    test('arbitrary triple byte', () => {
      expect(encode([0x100000])).toEqual([0xc0, 0x80, 0]);
    });

    test('largest triple byte', () => {
      expect(encode([0x1fffff])).toEqual([0xff, 0xff, 0x7f]);
    });

    test('smallest quadruple byte', () => {
      expect(encode([0x200000])).toEqual([0x81, 0x80, 0x80, 0]);
    });

    test('arbitrary quadruple byte', () => {
      expect(encode([0x8000000])).toEqual([0xc0, 0x80, 0x80, 0]);
    });

    test('largest quadruple byte', () => {
      expect(encode([0xfffffff])).toEqual([0xff, 0xff, 0xff, 0x7f]);
    });

    test('smallest quintuple byte', () => {
      expect(encode([0x10000000])).toEqual([0x81, 0x80, 0x80, 0x80, 0]);
    });

    test('arbitrary quintuple byte', () => {
      expect(encode([0xff000000])).toEqual([0x8f, 0xf8, 0x80, 0x80, 0]);
    });

    test('maximum 32-bit integer input', () => {
      expect(encode([0xffffffff])).toEqual([0x8f, 0xff, 0xff, 0xff, 0x7f]);
    });

    test('two single-byte values', () => {
      expect(encode([0x40, 0x7f])).toEqual([0x40, 0x7f]);
    });

    test('two multi-byte values', () => {
      expect(encode([0x4000, 0x123456])).toEqual([
        0x81, 0x80, 0, 0xc8, 0xe8, 0x56,
      ]);
    });

    test('many multi-byte values', () => {
      const input = [0x2000, 0x123456, 0xfffffff, 0, 0x3fff, 0x4000];
      const expected = [
        0xc0, 0, 0xc8, 0xe8, 0x56, 0xff, 0xff, 0xff, 0x7f, 0, 0xff, 0x7f, 0x81,
        0x80, 0,
      ];
      expect(encode(input)).toEqual(expected);
    });
  });

  describe('Decode a series of bytes, producing a series of integers.', () => {
    test('one byte', () => {
      expect(decode([0x7f])).toEqual([0x7f]);
    });

    test('two bytes', () => {
      expect(decode([0xc0, 0])).toEqual([0x2000]);
    });

    test('three bytes', () => {
      expect(decode([0xff, 0xff, 0x7f])).toEqual([0x1fffff]);
    });

    test('four bytes', () => {
      expect(decode([0x81, 0x80, 0x80, 0])).toEqual([0x200000]);
    });

    test('maximum 32-bit integer', () => {
      expect(decode([0x8f, 0xff, 0xff, 0xff, 0x7f])).toEqual([0xffffffff]);
    });

    test('incomplete sequence causes error', () => {
      expect(() => {
        decode([0xff]);
      }).toThrow(new Error('Incomplete sequence'));
    });

    test('incomplete sequence causes error, even if value is zero', () => {
      expect(() => {
        decode([0x80]);
      }).toThrow(new Error('Incomplete sequence'));
    });

    test('multiple values', () => {
      const input = [
        0xc0, 0, 0xc8, 0xe8, 0x56, 0xff, 0xff, 0xff, 0x7f, 0, 0xff, 0x7f, 0x81,
        0x80, 0,
      ];
      const expected = [0x2000, 0x123456, 0xfffffff, 0, 0x3fff, 0x4000];
      expect(decode(input)).toEqual(expected);
    });
  });
});
