//
// This is only a SKELETON file for the 'Meetup' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const meetup = (year, month, week, weekday) => {
  const weekdaysMap = {
    'Sunday': 0,
    'Monday': 1,
    'Tuesday': 2,
    'Wednesday': 3,
    'Thursday': 4,
    'Friday': 5,
    'Saturday': 6
  };
  const targetDay = weekdaysMap[weekday];
  const adjustedMonth = month - 1;
  const lastDay = new Date(year, adjustedMonth + 1, 0).getDate();
  const candidates = [];
  for (let day = 1; day <= lastDay; day++) {
    const currentDate = new Date(year, adjustedMonth, day);
    if (currentDate.getDay() === targetDay) {
      candidates.push(day);
    }
  }
  let selectedDay;
  switch (week) {
    case 'first':
      selectedDay = candidates[0];
      break;
    case 'second':
      selectedDay = candidates[1];
      break;
    case 'third':
      selectedDay = candidates[2];
      break;
    case 'fourth':
      selectedDay = candidates[3];
      break;
    case 'last':
      selectedDay = candidates[candidates.length - 1];
      break;
    case 'teenth':
      selectedDay = candidates.find(day => day >= 13 && day <= 19);
      break;
    default:
      throw new Error('Invalid week descriptor');
  }
  return new Date(year, adjustedMonth, selectedDay);
};
