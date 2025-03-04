export const recite = (startVerse = 1, endVerse = startVerse) => {
  const ordinals = [
    'first', 'second', 'third', 'fourth', 'fifth', 'sixth', 
    'seventh', 'eighth', 'ninth', 'tenth', 'eleventh', 'twelfth'
  ];
  
  const gifts = [
    'a Partridge in a Pear Tree',
    'two Turtle Doves',
    'three French Hens',
    'four Calling Birds',
    'five Gold Rings',
    'six Geese-a-Laying',
    'seven Swans-a-Swimming',
    'eight Maids-a-Milking',
    'nine Ladies Dancing',
    'ten Lords-a-Leaping',
    'eleven Pipers Piping',
    'twelve Drummers Drumming'
  ];

  let verses = [];
  
  // Adjust for zero-based index
  const startDay = startVerse - 1;
  const endDay = endVerse - 1;
  
  for (let day = startDay; day <= endDay; day++) {
    let verse = `On the ${ordinals[day]} day of Christmas my true love gave to me: `;
    
    // Build gifts in reverse order
    for (let i = day; i >= 0; i--) {
      if (day > 0 && i === 0) {
        verse += 'and ';
      }
      verse += gifts[i];
      if (i > 0) {
        verse += ', ';
      }
    }
    
    verse += '.\n';
    verses.push(verse);
  }
  
  return verses.join('\n');
};
