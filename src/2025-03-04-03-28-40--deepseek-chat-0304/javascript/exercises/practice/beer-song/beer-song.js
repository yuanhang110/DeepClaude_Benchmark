export const recite = (initialBottlesCount, takeDownCount) => {
  let lyrics = [];
  
  for (let i = 0; i < takeDownCount; i++) {
    let current = initialBottlesCount - i;
    let next = current - 1;
    
    // First line
    if (current === 0) {
      lyrics.push("No more bottles of beer on the wall, no more bottles of beer.");
    } else if (current === 1) {
      lyrics.push("1 bottle of beer on the wall, 1 bottle of beer.");
    } else {
      lyrics.push(`${current} bottles of beer on the wall, ${current} bottles of beer.`);
    }
    
    // Second line
    if (next < 0) {
      lyrics.push("Go to the store and buy some more, 99 bottles of beer on the wall.");
    } else if (next === 0) {
      lyrics.push("Take it down and pass it around, no more bottles of beer on the wall.");
    } else if (next === 1) {
      lyrics.push("Take one down and pass it around, 1 bottle of beer on the wall.");
    } else {
      lyrics.push(`Take one down and pass it around, ${next} bottles of beer on the wall.`);
    }
    
    // Add empty line between verses except last one
    if (i < takeDownCount - 1) {
      lyrics.push("");
    }
  }
  
  return lyrics;
};
