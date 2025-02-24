import { createEntry, formatEntries } from './ledger';

describe('Ledger', () => {
  test('empty ledger', () => {
    let currency = 'USD';
    let locale = 'en-US';
    let entries = [];
    let expected = [
      'Date       | Description               | Change       ',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('one entry', () => {
    let currency = 'USD';
    let locale = 'en-US';
    let entries = [createEntry('2015-01-01', 'Buy present', -1000)];
    let expected = [
      'Date       | Description               | Change       ',
      '01/01/2015 | Buy present               |      ($10.00)',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('credit and debit', () => {
    let currency = 'USD';
    let locale = 'en-US';
    let entries = [
      createEntry('2015-01-02', 'Get present', 1000),
      createEntry('2015-01-01', 'Buy present', -1000),
    ];
    let expected = [
      'Date       | Description               | Change       ',
      '01/01/2015 | Buy present               |      ($10.00)',
      '01/02/2015 | Get present               |       $10.00 ',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('final order tie breaker is change', () => {
    let currency = 'USD';
    let locale = 'en-US';
    let entries = [
      createEntry('2015-01-01', 'Something', 0),
      createEntry('2015-01-01', 'Something', -1),
      createEntry('2015-01-01', 'Something', 1),
    ];
    let expected = [
      'Date       | Description               | Change       ',
      '01/01/2015 | Something                 |       ($0.01)',
      '01/01/2015 | Something                 |        $0.00 ',
      '01/01/2015 | Something                 |        $0.01 ',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('overlong description is truncated', () => {
    let currency = 'USD';
    let locale = 'en-US';
    let entries = [
      createEntry('2015-01-01', 'Freude schoner Gotterfunken', -123456),
    ];
    let expected = [
      'Date       | Description               | Change       ',
      '01/01/2015 | Freude schoner Gotterf... |   ($1,234.56)',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('euros', () => {
    let currency = 'EUR';
    let locale = 'en-US';
    let entries = [createEntry('2015-01-01', 'Buy present', -1000)];
    let expected = [
      'Date       | Description               | Change       ',
      '01/01/2015 | Buy present               |      (€10.00)',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('Dutch locale', () => {
    let currency = 'USD';
    let locale = 'nl-NL';
    let entries = [createEntry('2015-03-12', 'Buy present', 123456)];
    let expected = [
      'Datum      | Omschrijving              | Verandering  ',
      '12-03-2015 | Buy present               |   $ 1.234,56 ',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('Dutch locale and euros', () => {
    let currency = 'EUR';
    let locale = 'nl-NL';
    let entries = [createEntry('2015-03-12', 'Buy present', 123456)];
    let expected = [
      'Datum      | Omschrijving              | Verandering  ',
      '12-03-2015 | Buy present               |   € 1.234,56 ',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('Dutch negative number with 3 digits before decimal point', () => {
    let currency = 'USD';
    let locale = 'nl-NL';
    let entries = [createEntry('2015-03-12', 'Buy present', -12345)];
    let expected = [
      'Datum      | Omschrijving              | Verandering  ',
      '12-03-2015 | Buy present               |    $ -123,45 ',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('American negative number with 3 digits before decimal point', () => {
    let currency = 'USD';
    let locale = 'en-US';
    let entries = [createEntry('2015-03-12', 'Buy present', -12345)];
    let expected = [
      'Date       | Description               | Change       ',
      '03/12/2015 | Buy present               |     ($123.45)',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });

  test('multiple entries on same date ordered by description', () => {
    let currency = 'USD';
    let locale = 'en-US';
    let entries = [
      createEntry('2015-01-01', 'Get present', 1000),
      createEntry('2015-01-01', 'Buy present', -1000),
    ];
    let expected = [
      'Date       | Description               | Change       ',
      '01/01/2015 | Buy present               |      ($10.00)',
      '01/01/2015 | Get present               |       $10.00 ',
    ].join('\n');
    expect(formatEntries(currency, locale, entries)).toEqual(expected);
  });
});
