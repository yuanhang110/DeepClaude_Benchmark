import { count } from './rectangles';

describe('Rectangles', () => {
  test('no rows', () => {
    const expected = 0;
    const actual = count([]);

    expect(actual).toEqual(expected);
  });

  test('no columns', () => {
    const expected = 0;
    const actual = count(['']);

    expect(actual).toEqual(expected);
  });

  test('no rectangles', () => {
    const expected = 0;
    const actual = count([' ']);

    expect(actual).toEqual(expected);
  });

  test('one rectangle', () => {
    const expected = 1;
    const actual = count(['+-+', '| |', '+-+']);

    expect(actual).toEqual(expected);
  });

  test('two rectangles without shared parts', () => {
    const expected = 2;
    const actual = count(['  +-+', '  | |', '+-+-+', '| |  ', '+-+  ']);

    expect(actual).toEqual(expected);
  });

  test('five rectangles with shared parts', () => {
    const expected = 5;
    const actual = count(['  +-+', '  | |', '+-+-+', '| | |', '+-+-+']);

    expect(actual).toEqual(expected);
  });

  test('rectangle of height 1 is counted', () => {
    const expected = 1;
    const actual = count(['+--+', '+--+']);

    expect(actual).toEqual(expected);
  });

  test('rectangle of width 1 is counted', () => {
    const expected = 1;
    const actual = count(['++', '||', '++']);

    expect(actual).toEqual(expected);
  });

  test('1x1 square is counted', () => {
    const expected = 1;
    const actual = count(['++', '++']);

    expect(actual).toEqual(expected);
  });

  test('only complete rectangles are counted', () => {
    const expected = 1;
    const actual = count(['  +-+', '    |', '+-+-+', '| | -', '+-+-+']);

    expect(actual).toEqual(expected);
  });

  test('rectangles can be of different sizes', () => {
    const expected = 3;
    const actual = count([
      '+------+----+',
      '|      |    |',
      '+---+--+    |',
      '|   |       |',
      '+---+-------+',
    ]);

    expect(actual).toEqual(expected);
  });

  test('corner is required for a rectangle to be complete', () => {
    const expected = 2;
    const actual = count([
      '+------+----+',
      '|      |    |',
      '+------+    |',
      '|   |       |',
      '+---+-------+',
    ]);

    expect(actual).toEqual(expected);
  });

  test('large input with many rectangles', () => {
    const expected = 60;
    const actual = count([
      '+---+--+----+',
      '|   +--+----+',
      '+---+--+    |',
      '|   +--+----+',
      '+---+--+--+-+',
      '+---+--+--+-+',
      '+------+  | |',
      '          +-+',
    ]);

    expect(actual).toEqual(expected);
  });

  test('rectangles must have four sides', () => {
    const expected = 5;
    const actual = count([
      '+-+ +-+',
      '| | | |',
      '+-+-+-+',
      '  | |  ',
      '+-+-+-+',
      '| | | |',
      '+-+ +-+',
    ]);

    expect(actual).toEqual(expected);
  });
});
