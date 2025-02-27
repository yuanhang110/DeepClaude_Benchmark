//
// This is only a SKELETON file for the 'Twelve Days' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (startVerse = 1, endVerse) => {
  if (endVerse === undefined) {
    endVerse = startVerse;
  }

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

  const verses = [];

  for (let day = startVerse; day <= endVerse; day++) {
    const dayIndex = day - 1;
    const currentGifts = gifts.slice(0, day).reverse();
    let giftsText;

    if (currentGifts.length === 1) {
      giftsText = currentGifts[0];
    } else {
      const allButLast = currentGifts.slice(0, -1).join(', ');
      const last = currentGifts[currentGifts.length - 1];
      giftsText = `${allButLast}, and ${last}`;
    }

    verses.push(`On the ${ordinals[dayIndex]} day of Christmas my true love gave to me: ${giftsText}.\n`);
  }

  return verses.join('\n');
};
