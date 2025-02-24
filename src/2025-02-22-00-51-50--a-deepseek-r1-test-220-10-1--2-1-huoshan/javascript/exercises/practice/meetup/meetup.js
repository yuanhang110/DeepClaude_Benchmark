//
// This is only a SKELETON file for the 'Meetup' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const meetup = (year, month, descriptor, weekday) => {
  const weekdays = {
    Sunday: 0,
    Monday: 1,
    Tuesday: 2,
    Wednesday: 3,
    Thursday: 4,
    Friday: 5,
    Saturday: 6
  };
  
  const targetWeekday = weekdays[weekday];
  const adjustedMonth = month - 1; // Convert to 0-based index

  if (descriptor === 'teenth') {
    for (let day = 13; day <= 19; day++) {
      const date = new Date(year, adjustedMonth, day);
      if (date.getDay() === targetWeekday) {
        return date;
      }
    }
  }

  // For first, second, third, fourth, and last
  const dates = [];
  for (let day = 1; day <= 31; day++) {
    const date = new Date(year, adjustedMonth, day);
    if (date.getMonth() !== adjustedMonth) break;
    if (date.getDay() === targetWeekday) {
      dates.push(date);
    }
  }

  switch (descriptor) {
    case 'first':
      return dates[0];
    case 'second':
      return dates[1];
    case 'third':
      return dates[2];
    case 'fourth':
      return dates[3];
    case 'last':
      return dates[dates.length - 1];
    default:
      throw new Error('Invalid descriptor');
  }
};
