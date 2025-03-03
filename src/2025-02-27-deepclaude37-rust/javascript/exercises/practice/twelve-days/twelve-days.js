//
// This is only a SKELETON file for the 'Twelve Days' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (startVerse, endVerse) => {
  // Handle defaults: if no parameters given, do whole song
  // if only startVerse given, do just that verse
  if (startVerse === undefined) {
    startVerse = 1;
    endVerse = 12;
  } else if (endVerse === undefined) {
    endVerse = startVerse;
  }
  
  const ordinals = ['first', 'second', 'third', 'fourth', 'fifth', 'sixth', 'seventh', 'eighth', 'ninth', 'tenth', 'eleventh', 'twelfth'];
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
  for (let i = startVerse - 1; i < endVerse; i++) {
    const day = i + 1;
    const currentOrdinal = ordinals[i];
    const currentGifts = gifts.slice(0, day).reverse();
    let giftsList;
    if (currentGifts.length === 1) {
      giftsList = currentGifts[0];
    } else {
      const allButLast = currentGifts.slice(0, -1).join(', ');
      giftsList = `${allButLast}, and ${currentGifts[currentGifts.length - 1]}`;
    }
    const verse = `On the ${currentOrdinal} day of Christmas my true love gave to me: ${giftsList}.`;
    verses.push(verse);
  }

  return verses.join('\n\n');
};
