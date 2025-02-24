export const recite = (startVerse, endVerse) => {
  // Handle default parameters
  if (typeof startVerse === 'undefined') {
    startVerse = 1;
    endVerse = 12;
  } else if (typeof endVerse === 'undefined') {
    endVerse = startVerse;
  }
  const ordinals = ['first', 'second', 'third', 'fourth', 'fifth', 'sixth',
    'seventh', 'eighth', 'ninth', 'tenth', 'eleventh', 'twelfth'];
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

  const verses = [];
  for (let day = startVerse; day <= endVerse; day++) {
    const ordinal = ordinals[day - 1];
    const selectedGifts = gifts.slice(0, day).reverse();
    let giftsList;
    if (selectedGifts.length === 1) {
      giftsList = selectedGifts[0];
    } else {
      const allButLast = selectedGifts.slice(0, -1).join(', ');
      const last = selectedGifts.slice(-1)[0];
      giftsList = `${allButLast}, and ${last}`;
    }
    verses.push(`On the ${ordinal} day of Christmas my true love gave to me: ${giftsList}.\n`);
  }
  return verses.join('\n\n');
};
