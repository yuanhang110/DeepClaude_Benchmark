//
// This is only a SKELETON file for the 'Pig Latin' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const translate = (input) => {
  return input.split(' ').map(word => {
    let match;
    // Rule 1: Check for vowel, xr, or yt at start
    if (/^(?:[aeiou]|xr|yt)/.test(word)) {
      return word + 'ay';
    }
    // Rule 3: Check for consonant(s) followed by 'qu'
    if ((match = word.match(/^([^aeiou]*qu)(.*)/))) {
      return match[2] + match[1] + 'ay';
    }
    // Rule 4: Check for consonant(s) followed by 'y'
    if ((match = word.match(/^([^aeiou]+)(y.*)/))) {
      return match[2] + match[1] + 'ay';
    }
    // Rule 2: Move leading consonant(s) to end
    if ((match = word.match(/^([^aeiou]+)(.*)/))) {
      return match[2] + match[1] + 'ay';
    }
    return word; // Fallback for empty/edge cases
  }).join(' ');
};
