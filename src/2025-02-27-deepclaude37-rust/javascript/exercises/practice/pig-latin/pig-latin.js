//
// This is only a SKELETON file for the 'Pig Latin' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const translate = (text) => {
  return text.split(' ').map(word => {
    const lowerWord = word.toLowerCase();
    let match;

    // Rule 1: Check for words starting with vowels, "xr", or "yt"
    if (/^(?:[aeiou]|xr|yt)/.test(lowerWord)) {
      return lowerWord + 'ay';
    }

    // Rule 3: Check for consonants followed by "qu"
    if ((match = /^([^aeiou]*qu)(.*)/.exec(lowerWord))) {
      return match[2] + match[1] + 'ay';
    }

    // Rule 4: Check for consonants followed by "y"
    if ((match = /^([^aeiou]+)(y.*)/.exec(lowerWord))) {
      return match[2] + match[1] + 'ay';
    }

    // Rule 2: Move consonants to end
    if ((match = /^([^aeiou]+)(.*)/.exec(lowerWord))) {
      return match[2] + match[1] + 'ay';
    }

    return lowerWord + 'ay';
  }).join(' ');
};
