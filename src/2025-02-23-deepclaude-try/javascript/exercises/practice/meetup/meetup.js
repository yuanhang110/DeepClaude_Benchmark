const DAYS = {
  Sunday: 0,
  Monday: 1,
  Tuesday: 2,
  Wednesday: 3,
  Thursday: 4,
  Friday: 5,
  Saturday: 6
};

export const meetup = (year, month, week, weekday) => {
  // Convert month to 0-based index
  const monthIndex = month - 1;
  const targetDay = DAYS[weekday];
  
  // Handle teenth case
  if (week === 'teenth') {
    for (let day = 13; day <= 19; day++) {
      const date = new Date(year, monthIndex, day);
      if (date.getDay() === targetDay) {
        return date;
      }
    }
  }
  
  // Get all matching weekdays in the month
  const matchingDays = [];
  const lastDay = new Date(year, monthIndex + 1, 0).getDate();
  
  for (let day = 1; day <= lastDay; day++) {
    const date = new Date(year, monthIndex, day);
    if (date.getDay() === targetDay) {
      matchingDays.push(date);
    }
  }

  // Return the appropriate date based on the week descriptor
  switch (week) {
    case 'first':
      return matchingDays[0];
    case 'second':
      return matchingDays[1];
    case 'third':
      return matchingDays[2];
    case 'fourth':
      return matchingDays[3];
    case 'last':
      return matchingDays[matchingDays.length - 1];
    default:
      throw new Error('Invalid week descriptor');
  }
};
