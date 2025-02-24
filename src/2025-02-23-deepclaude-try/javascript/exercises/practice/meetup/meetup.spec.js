import { meetup } from './meetup';

describe('Meetup', () => {
  test('monteenth of May 2013', () => {
    expect(meetup(2013, 5, 'teenth', 'Monday')).toEqual(new Date(2013, 4, 13));
  });

  test('monteenth of August 2013', () => {
    expect(meetup(2013, 8, 'teenth', 'Monday')).toEqual(new Date(2013, 7, 19));
  });

  test('monteenth of September 2013', () => {
    expect(meetup(2013, 9, 'teenth', 'Monday')).toEqual(new Date(2013, 8, 16));
  });

  test('tuesteenth of March 2013', () => {
    expect(meetup(2013, 3, 'teenth', 'Tuesday')).toEqual(new Date(2013, 2, 19));
  });

  test('tuesteenth of April 2013', () => {
    expect(meetup(2013, 4, 'teenth', 'Tuesday')).toEqual(new Date(2013, 3, 16));
  });

  test('tuesteenth of August 2013', () => {
    expect(meetup(2013, 8, 'teenth', 'Tuesday')).toEqual(new Date(2013, 7, 13));
  });

  test('wednesteenth of January 2013', () => {
    expect(meetup(2013, 1, 'teenth', 'Wednesday')).toEqual(
      new Date(2013, 0, 16),
    );
  });

  test('wednesteenth of February 2013', () => {
    expect(meetup(2013, 2, 'teenth', 'Wednesday')).toEqual(
      new Date(2013, 1, 13),
    );
  });

  test('wednesteenth of June 2013', () => {
    expect(meetup(2013, 6, 'teenth', 'Wednesday')).toEqual(
      new Date(2013, 5, 19),
    );
  });

  test('thursteenth of May 2013', () => {
    expect(meetup(2013, 5, 'teenth', 'Thursday')).toEqual(
      new Date(2013, 4, 16),
    );
  });

  test('thursteenth of June 2013', () => {
    expect(meetup(2013, 6, 'teenth', 'Thursday')).toEqual(
      new Date(2013, 5, 13),
    );
  });

  test('thursteenth of September 2013', () => {
    expect(meetup(2013, 9, 'teenth', 'Thursday')).toEqual(
      new Date(2013, 8, 19),
    );
  });

  test('friteenth of April 2013', () => {
    expect(meetup(2013, 4, 'teenth', 'Friday')).toEqual(new Date(2013, 3, 19));
  });

  test('friteenth of August 2013', () => {
    expect(meetup(2013, 8, 'teenth', 'Friday')).toEqual(new Date(2013, 7, 16));
  });

  test('friteenth of September 2013', () => {
    expect(meetup(2013, 9, 'teenth', 'Friday')).toEqual(new Date(2013, 8, 13));
  });

  test('saturteenth of February 2013', () => {
    expect(meetup(2013, 2, 'teenth', 'Saturday')).toEqual(
      new Date(2013, 1, 16),
    );
  });

  test('saturteenth of April 2013', () => {
    expect(meetup(2013, 4, 'teenth', 'Saturday')).toEqual(
      new Date(2013, 3, 13),
    );
  });

  test('saturteenth of October 2013', () => {
    expect(meetup(2013, 10, 'teenth', 'Saturday')).toEqual(
      new Date(2013, 9, 19),
    );
  });

  test('sunteenth of May 2013', () => {
    expect(meetup(2013, 5, 'teenth', 'Sunday')).toEqual(new Date(2013, 4, 19));
  });

  test('sunteenth of June 2013', () => {
    expect(meetup(2013, 6, 'teenth', 'Sunday')).toEqual(new Date(2013, 5, 16));
  });

  test('sunteenth of October 2013', () => {
    expect(meetup(2013, 10, 'teenth', 'Sunday')).toEqual(new Date(2013, 9, 13));
  });

  test('first Monday of March 2013', () => {
    expect(meetup(2013, 3, 'first', 'Monday')).toEqual(new Date(2013, 2, 4));
  });

  test('first Monday of April 2013', () => {
    expect(meetup(2013, 4, 'first', 'Monday')).toEqual(new Date(2013, 3, 1));
  });

  test('first Tuesday of May 2013', () => {
    expect(meetup(2013, 5, 'first', 'Tuesday')).toEqual(new Date(2013, 4, 7));
  });

  test('first Tuesday of June 2013', () => {
    expect(meetup(2013, 6, 'first', 'Tuesday')).toEqual(new Date(2013, 5, 4));
  });

  test('first Wednesday of July 2013', () => {
    expect(meetup(2013, 7, 'first', 'Wednesday')).toEqual(new Date(2013, 6, 3));
  });

  test('first Wednesday of August 2013', () => {
    expect(meetup(2013, 8, 'first', 'Wednesday')).toEqual(new Date(2013, 7, 7));
  });

  test('first Thursday of September 2013', () => {
    expect(meetup(2013, 9, 'first', 'Thursday')).toEqual(new Date(2013, 8, 5));
  });

  test('first Thursday of October 2013', () => {
    expect(meetup(2013, 10, 'first', 'Thursday')).toEqual(new Date(2013, 9, 3));
  });

  test('first Friday of November 2013', () => {
    expect(meetup(2013, 11, 'first', 'Friday')).toEqual(new Date(2013, 10, 1));
  });

  test('first Friday of December 2013', () => {
    expect(meetup(2013, 12, 'first', 'Friday')).toEqual(new Date(2013, 11, 6));
  });

  test('first Saturday of January 2013', () => {
    expect(meetup(2013, 1, 'first', 'Saturday')).toEqual(new Date(2013, 0, 5));
  });

  test('first Saturday of February 2013', () => {
    expect(meetup(2013, 2, 'first', 'Saturday')).toEqual(new Date(2013, 1, 2));
  });

  test('first Sunday of March 2013', () => {
    expect(meetup(2013, 3, 'first', 'Sunday')).toEqual(new Date(2013, 2, 3));
  });

  test('first Sunday of April 2013', () => {
    expect(meetup(2013, 4, 'first', 'Sunday')).toEqual(new Date(2013, 3, 7));
  });

  test('second Monday of March 2013', () => {
    expect(meetup(2013, 3, 'second', 'Monday')).toEqual(new Date(2013, 2, 11));
  });

  test('second Monday of April 2013', () => {
    expect(meetup(2013, 4, 'second', 'Monday')).toEqual(new Date(2013, 3, 8));
  });

  test('second Tuesday of May 2013', () => {
    expect(meetup(2013, 5, 'second', 'Tuesday')).toEqual(new Date(2013, 4, 14));
  });

  test('second Tuesday of June 2013', () => {
    expect(meetup(2013, 6, 'second', 'Tuesday')).toEqual(new Date(2013, 5, 11));
  });

  test('second Wednesday of July 2013', () => {
    expect(meetup(2013, 7, 'second', 'Wednesday')).toEqual(
      new Date(2013, 6, 10),
    );
  });

  test('second Wednesday of August 2013', () => {
    expect(meetup(2013, 8, 'second', 'Wednesday')).toEqual(
      new Date(2013, 7, 14),
    );
  });

  test('second Thursday of September 2013', () => {
    expect(meetup(2013, 9, 'second', 'Thursday')).toEqual(
      new Date(2013, 8, 12),
    );
  });

  test('second Thursday of October 2013', () => {
    expect(meetup(2013, 10, 'second', 'Thursday')).toEqual(
      new Date(2013, 9, 10),
    );
  });

  test('second Friday of November 2013', () => {
    expect(meetup(2013, 11, 'second', 'Friday')).toEqual(new Date(2013, 10, 8));
  });

  test('second Friday of December 2013', () => {
    expect(meetup(2013, 12, 'second', 'Friday')).toEqual(
      new Date(2013, 11, 13),
    );
  });

  test('second Saturday of January 2013', () => {
    expect(meetup(2013, 1, 'second', 'Saturday')).toEqual(
      new Date(2013, 0, 12),
    );
  });

  test('second Saturday of February 2013', () => {
    expect(meetup(2013, 2, 'second', 'Saturday')).toEqual(new Date(2013, 1, 9));
  });

  test('second Sunday of March 2013', () => {
    expect(meetup(2013, 3, 'second', 'Sunday')).toEqual(new Date(2013, 2, 10));
  });

  test('second Sunday of April 2013', () => {
    expect(meetup(2013, 4, 'second', 'Sunday')).toEqual(new Date(2013, 3, 14));
  });

  test('third Monday of March 2013', () => {
    expect(meetup(2013, 3, 'third', 'Monday')).toEqual(new Date(2013, 2, 18));
  });

  test('third Monday of April 2013', () => {
    expect(meetup(2013, 4, 'third', 'Monday')).toEqual(new Date(2013, 3, 15));
  });

  test('third Tuesday of May 2013', () => {
    expect(meetup(2013, 5, 'third', 'Tuesday')).toEqual(new Date(2013, 4, 21));
  });

  test('third Tuesday of June 2013', () => {
    expect(meetup(2013, 6, 'third', 'Tuesday')).toEqual(new Date(2013, 5, 18));
  });

  test('third Wednesday of July 2013', () => {
    expect(meetup(2013, 7, 'third', 'Wednesday')).toEqual(
      new Date(2013, 6, 17),
    );
  });

  test('third Wednesday of August 2013', () => {
    expect(meetup(2013, 8, 'third', 'Wednesday')).toEqual(
      new Date(2013, 7, 21),
    );
  });

  test('third Thursday of September 2013', () => {
    expect(meetup(2013, 9, 'third', 'Thursday')).toEqual(new Date(2013, 8, 19));
  });

  test('third Thursday of October 2013', () => {
    expect(meetup(2013, 10, 'third', 'Thursday')).toEqual(
      new Date(2013, 9, 17),
    );
  });

  test('third Friday of November 2013', () => {
    expect(meetup(2013, 11, 'third', 'Friday')).toEqual(new Date(2013, 10, 15));
  });

  test('third Friday of December 2013', () => {
    expect(meetup(2013, 12, 'third', 'Friday')).toEqual(new Date(2013, 11, 20));
  });

  test('third Saturday of January 2013', () => {
    expect(meetup(2013, 1, 'third', 'Saturday')).toEqual(new Date(2013, 0, 19));
  });

  test('third Saturday of February 2013', () => {
    expect(meetup(2013, 2, 'third', 'Saturday')).toEqual(new Date(2013, 1, 16));
  });

  test('third Sunday of March 2013', () => {
    expect(meetup(2013, 3, 'third', 'Sunday')).toEqual(new Date(2013, 2, 17));
  });

  test('third Sunday of April 2013', () => {
    expect(meetup(2013, 4, 'third', 'Sunday')).toEqual(new Date(2013, 3, 21));
  });

  test('fourth Monday of March 2013', () => {
    expect(meetup(2013, 3, 'fourth', 'Monday')).toEqual(new Date(2013, 2, 25));
  });

  test('fourth Monday of April 2013', () => {
    expect(meetup(2013, 4, 'fourth', 'Monday')).toEqual(new Date(2013, 3, 22));
  });

  test('fourth Tuesday of May 2013', () => {
    expect(meetup(2013, 5, 'fourth', 'Tuesday')).toEqual(new Date(2013, 4, 28));
  });

  test('fourth Tuesday of June 2013', () => {
    expect(meetup(2013, 6, 'fourth', 'Tuesday')).toEqual(new Date(2013, 5, 25));
  });

  test('fourth Wednesday of July 2013', () => {
    expect(meetup(2013, 7, 'fourth', 'Wednesday')).toEqual(
      new Date(2013, 6, 24),
    );
  });

  test('fourth Wednesday of August 2013', () => {
    expect(meetup(2013, 8, 'fourth', 'Wednesday')).toEqual(
      new Date(2013, 7, 28),
    );
  });

  test('fourth Thursday of September 2013', () => {
    expect(meetup(2013, 9, 'fourth', 'Thursday')).toEqual(
      new Date(2013, 8, 26),
    );
  });

  test('fourth Thursday of October 2013', () => {
    expect(meetup(2013, 10, 'fourth', 'Thursday')).toEqual(
      new Date(2013, 9, 24),
    );
  });

  test('fourth Friday of November 2013', () => {
    expect(meetup(2013, 11, 'fourth', 'Friday')).toEqual(
      new Date(2013, 10, 22),
    );
  });

  test('fourth Friday of December 2013', () => {
    expect(meetup(2013, 12, 'fourth', 'Friday')).toEqual(
      new Date(2013, 11, 27),
    );
  });

  test('fourth Saturday of January 2013', () => {
    expect(meetup(2013, 1, 'fourth', 'Saturday')).toEqual(
      new Date(2013, 0, 26),
    );
  });

  test('fourth Saturday of February 2013', () => {
    expect(meetup(2013, 2, 'fourth', 'Saturday')).toEqual(
      new Date(2013, 1, 23),
    );
  });

  test('fourth Sunday of March 2013', () => {
    expect(meetup(2013, 3, 'fourth', 'Sunday')).toEqual(new Date(2013, 2, 24));
  });

  test('fourth Sunday of April 2013', () => {
    expect(meetup(2013, 4, 'fourth', 'Sunday')).toEqual(new Date(2013, 3, 28));
  });

  test('last Monday of March 2013', () => {
    expect(meetup(2013, 3, 'last', 'Monday')).toEqual(new Date(2013, 2, 25));
  });

  test('last Monday of April 2013', () => {
    expect(meetup(2013, 4, 'last', 'Monday')).toEqual(new Date(2013, 3, 29));
  });

  test('last Tuesday of May 2013', () => {
    expect(meetup(2013, 5, 'last', 'Tuesday')).toEqual(new Date(2013, 4, 28));
  });

  test('last Tuesday of June 2013', () => {
    expect(meetup(2013, 6, 'last', 'Tuesday')).toEqual(new Date(2013, 5, 25));
  });

  test('last Wednesday of July 2013', () => {
    expect(meetup(2013, 7, 'last', 'Wednesday')).toEqual(new Date(2013, 6, 31));
  });

  test('last Wednesday of August 2013', () => {
    expect(meetup(2013, 8, 'last', 'Wednesday')).toEqual(new Date(2013, 7, 28));
  });

  test('last Thursday of September 2013', () => {
    expect(meetup(2013, 9, 'last', 'Thursday')).toEqual(new Date(2013, 8, 26));
  });

  test('last Thursday of October 2013', () => {
    expect(meetup(2013, 10, 'last', 'Thursday')).toEqual(new Date(2013, 9, 31));
  });

  test('last Friday of November 2013', () => {
    expect(meetup(2013, 11, 'last', 'Friday')).toEqual(new Date(2013, 10, 29));
  });

  test('last Friday of December 2013', () => {
    expect(meetup(2013, 12, 'last', 'Friday')).toEqual(new Date(2013, 11, 27));
  });

  test('last Saturday of January 2013', () => {
    expect(meetup(2013, 1, 'last', 'Saturday')).toEqual(new Date(2013, 0, 26));
  });

  test('last Saturday of February 2013', () => {
    expect(meetup(2013, 2, 'last', 'Saturday')).toEqual(new Date(2013, 1, 23));
  });

  test('last Sunday of March 2013', () => {
    expect(meetup(2013, 3, 'last', 'Sunday')).toEqual(new Date(2013, 2, 31));
  });

  test('last Sunday of April 2013', () => {
    expect(meetup(2013, 4, 'last', 'Sunday')).toEqual(new Date(2013, 3, 28));
  });

  test('last Wednesday of February 2012', () => {
    expect(meetup(2012, 2, 'last', 'Wednesday')).toEqual(new Date(2012, 1, 29));
  });

  test('last Wednesday of December 2014', () => {
    expect(meetup(2014, 12, 'last', 'Wednesday')).toEqual(
      new Date(2014, 11, 31),
    );
  });

  test('last Sunday of February 2015', () => {
    expect(meetup(2015, 2, 'last', 'Sunday')).toEqual(new Date(2015, 1, 22));
  });

  test('first Friday of December 2012', () => {
    expect(meetup(2012, 12, 'first', 'Friday')).toEqual(new Date(2012, 11, 7));
  });
});
