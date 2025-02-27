//
// This is only a SKELETON file for the 'Pig Latin' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const translate = (sentence) => {
  return sentence.split(' ').map(word => {
    if (/^(?:[aeiou]|xr|yt)/i.test(word)) {
      return word + 'ay';
    }

    let match;
    if ((match = word.match(/^([^aeiou]*qu)(.*)/i))) {
      return match[2] + match[1] + 'ay';
    }

    if ((match = word.match(/^([^aeiou]+)(y.*)/i))) {
      return match[2] + match[1] + 'ay';
    }

    if ((match = word.match(/^([^aeiou]+)(.*)/i))) {
      return match[2] + match[1] + 'ay';
    }

    return word + 'ay';
  }).join(' ');
};
