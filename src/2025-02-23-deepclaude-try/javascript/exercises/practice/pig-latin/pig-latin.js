//
// This is only a SKELETON file for the 'Pig Latin' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const translate = (text) => {
  return text.split(' ').map(translateWord).join(' ');
};

const translateWord = (word) => {
  // Rule 1: Vowel, xr, yt at start
  if (/^(?:[aeiou]|xr|yt)/.test(word)) {
    return word + 'ay';
  }

  // Rule 3: consonants followed by qu
  const quMatch = word.match(/^([^aeiou]*qu)(.*)/);
  if (quMatch) {
    return quMatch[2] + quMatch[1] + 'ay';
  }

  // Rule 4: consonants followed by y
  const yMatch = word.match(/^([^aeiou]+)(y.*)/);
  if (yMatch) {
    return yMatch[2] + yMatch[1] + 'ay';
  }

  // Rule 2: consonants only at start
  const consonantMatch = word.match(/^([^aeiou]+)(.*)/);
  if (consonantMatch) {
    return consonantMatch[2] + consonantMatch[1] + 'ay';
  }

  // Fallback (shouldn't be reached for valid words)
  return word + 'ay';
};
