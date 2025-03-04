import { convert } from './ocr-numbers';

describe('ocr', () => {
  test('recognizes zero', () => {
    expect(
      // prettier-ignore
      convert(
        ' _ \n' +
        '| |\n' +
        '|_|\n' +
        '   '
      ),
    ).toBe('0');
  });

  test('recognizes one', () => {
    expect(
      // prettier-ignore
      convert(
        '   \n' +
        '  |\n' +
        '  |\n' +
        '   '
      ),
    ).toBe('1');
  });

  test('recognizes two', () => {
    expect(
      // prettier-ignore
      convert(
        ' _ \n' +
        ' _|\n' +
        '|_ \n' +
        '   '
      ),
    ).toBe('2');
  });

  test('recognizes three', () => {
    expect(
      // prettier-ignore
      convert(
        ' _ \n' +
        ' _|\n' +
        ' _|\n' +
        '   '
      ),
    ).toBe('3');
  });

  test('recognizes four', () => {
    expect(
      // prettier-ignore
      convert(
        '   \n' +
        '|_|\n' +
        '  |\n' +
        '   '
      ),
    ).toBe('4');
  });

  test('recognizes five', () => {
    expect(
      // prettier-ignore
      convert(
        ' _ \n' +
        '|_ \n' +
        ' _|\n' +
        '   '
      ),
    ).toBe('5');
  });

  test('recognizes six', () => {
    expect(
      // prettier-ignore
      convert(
        ' _ \n' +
        '|_ \n' +
        '|_|\n' +
        '   '
      ),
    ).toBe('6');
  });

  test('recognizes seven', () => {
    expect(
      // prettier-ignore
      convert(
        ' _ \n' +
        '  |\n' +
        '  |\n' +
        '   '
      ),
    ).toBe('7');
  });

  test('recognizes eight', () => {
    expect(
      // prettier-ignore
      convert(
        ' _ \n' +
        '|_|\n' +
        '|_|\n' +
        '   '
      ),
    ).toBe('8');
  });

  test('recognizes nine', () => {
    expect(
      // prettier-ignore
      convert(
        ' _ \n' +
        '|_|\n' +
        ' _|\n' +
        '   '
      ),
    ).toBe('9');
  });

  test('recognizes ten', () => {
    expect(
      // prettier-ignore
      convert(
        '    _ \n' +
        '  || |\n' +
        '  ||_|\n' +
        '      '
      ),
    ).toBe('10');
  });

  test('identifies garble', () => {
    expect(
      // prettier-ignore
      convert(
        '   \n' +
        '| |\n' +
        '| |\n' +
        '   '
      ),
    ).toBe('?');
  });

  test('converts 110101100', () => {
    expect(
      // prettier-ignore
      convert(
        '       _     _        _  _ \n' +
        '  |  || |  || |  |  || || |\n' +
        '  |  ||_|  ||_|  |  ||_||_|\n' +
        '                           '
      ),
    ).toBe('110101100');
  });

  test('identifies garble mixed in', () => {
    expect(
      // prettier-ignore
      convert(
        '       _     _           _ \n' +
        '  |  || |  || |     || || |\n' +
        '  |  | _|  ||_|  |  ||_||_|\n' +
        '                           '
      ),
    ).toBe('11?10?1?0');
  });

  test('converts 1234567890', () => {
    expect(
      // prettier-ignore
      convert(
        '    _  _     _  _  _  _  _  _ \n' +
        '  | _| _||_||_ |_   ||_||_|| |\n' +
        '  ||_  _|  | _||_|  ||_| _||_|\n' +
        '                              '
      ),
    ).toBe('1234567890');
  });

  test('converts 123 456 789', () => {
    expect(
      // prettier-ignore
      convert(
        '    _  _ \n' +
        '  | _| _|\n' +
        '  ||_  _|\n' +
        '         \n' +
        '    _  _ \n' +
        '|_||_ |_ \n' +
        '  | _||_|\n' +
        '         \n' +
        ' _  _  _ \n' +
        '  ||_||_|\n' +
        '  ||_| _|\n' +
        '         '
      ),
    ).toBe('123,456,789');
  });
});
