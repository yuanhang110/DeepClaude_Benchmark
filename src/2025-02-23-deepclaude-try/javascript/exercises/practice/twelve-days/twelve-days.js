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

  const verses = [];
  const start = startVerse - 1;
  const end = endVerse - 1;
  for (let i = start; i <= end; i++) {
    const currentGifts = gifts.slice(0, i + 1).reverse();
    let giftsList;
    if (currentGifts.length === 1) {
      giftsList = currentGifts[0];
    } else {
      giftsList = currentGifts.slice(0, -1).join(', ') + ', and ' + currentGifts[currentGifts.length - 1];
    }
    verses.push(`On the ${ordinals[i]} day of Christmas my true love gave to me: ${giftsList}.`);
  }
  return verses.join('\n\n') + '\n';
};
