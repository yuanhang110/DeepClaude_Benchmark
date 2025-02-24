//
// This is only a SKELETON file for the 'Beer Song' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

const generateVerse = (currentBottles) => {
  if (currentBottles === 0) {
    return "No more bottles of beer on the wall, no more bottles of beer.\nGo to the store and buy some more, 99 bottles of beer on the wall.\n";
  }

  const bottles = currentBottles === 1 ? "1 bottle" : `${currentBottles} bottles`;
  const nextBottles = currentBottles - 1;
  
  let nextLine;
  if (nextBottles === 0) {
    nextLine = "no more bottles of beer on the wall.\n";
  } else if (nextBottles === 1) {
    nextLine = "1 bottle of beer on the wall.\n";
  } else {
    nextLine = `${nextBottles} bottles of beer on the wall.\n`;
  }

  return `${bottles} of beer on the wall, ${bottles.toLowerCase()} of beer.\n` +
         `Take ${currentBottles === 1 ? "it" : "one"} down and pass it around, ${nextLine}`;
};

export const recite = (initialBottlesCount, takeDownCount = 1) => {
  const verses = [];
  for (let i = 0; i < takeDownCount; i++) {
    const current = initialBottlesCount - i;
    verses.push(generateVerse(current));
    if (current === 0) break;
  }
  return verses.join('\n\n');
};
