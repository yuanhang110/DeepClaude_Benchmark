export const meetup = (year, month, week, dayOfWeek) => {
  const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
  const targetDay = days.indexOf(dayOfWeek);
  
  if (week === 'teenth') {
    // Check days 13-19 for the matching weekday
    for (let day = 13; day <= 19; day++) {
      const date = new Date(year, month, day);
      if (date.getDay() === targetDay) {
        return date;
      }
    }
  } else {
    let startDay = 1;
    let endDay = new Date(year, month + 1, 0).getDate();
    
    if (week === 'last') {
      // Start from the end of the month and work backwards
      for (let day = endDay; day >= 1; day--) {
        const date = new Date(year, month, day);
        if (date.getDay() === targetDay) {
          return date;
        }
      }
    } else {
      // Handle first, second, third, fourth
      const weekOffset = ['first', 'second', 'third', 'fourth'].indexOf(week);
      let found = 0;
      
      for (let day = 1; day <= endDay; day++) {
        const date = new Date(year, month, day);
        if (date.getDay() === targetDay) {
          if (found === weekOffset) {
            return date;
          }
          found++;
        }
      }
    }
  }
  
  throw new Error('No valid date found');
};
