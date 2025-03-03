//
// This is only a SKELETON file for the 'Meetup' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const meetup = (year, month, descriptor, weekday) => {
  const weekdays = {
    'sunday': 0,
    'monday': 1,
    'tuesday': 2,
    'wednesday': 3,
    'thursday': 4,
    'friday': 5,
    'saturday': 6
  };

  const targetWeekday = weekdays[weekday.toLowerCase()];
  const descriptorLower = descriptor.toLowerCase();

  if (descriptorLower === 'teenth') {
    for (let day = 13; day <= 19; day++) {
      const date = new Date(year, month - 1, day);
      if (date.getDay() === targetWeekday) {
        return date;
      }
    }
  } else {
    const lastDay = new Date(year, month + 1, 0).getDate();
    const candidates = [];
    for (let day = 1; day <= lastDay; day++) {
      const date = new Date(year, month - 1, day);
      if (date.getDay() === targetWeekday) {
        candidates.push(date);
      }
    }

    let index;
    switch (descriptorLower) {
      case 'first':
        index = 0;
        break;
      case 'second':
        index = 1;
        break;
      case 'third':
        index = 2;
        break;
      case 'fourth':
        index = 3;
        break;
      case 'last':
        index = candidates.length - 1;
        break;
      default:
        throw new Error('Invalid descriptor');
    }

    if (index >= candidates.length) {
      throw new Error('No such date');
    }

    return candidates[index];
  }
};
