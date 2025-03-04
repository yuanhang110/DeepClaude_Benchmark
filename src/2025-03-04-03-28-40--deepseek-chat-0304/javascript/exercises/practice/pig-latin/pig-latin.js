const isVowel = (char) => /[aeiou]/i.test(char);

const startsWithVowelLike = (word) => 
  isVowel(word[0]) || word.startsWith('xr') || word.startsWith('yt');

const getConsonantCluster = (word) => {
  // Handle 'qu' cases
  const quIndex = word.indexOf('qu');
  if (quIndex !== -1) {
    return word.slice(0, quIndex + 2);
  }
  
  // Handle 'y' as consonant at start
  if (word[0] === 'y') {
    return 'y';
  }
  
  // Handle 'y' after consonants
  const yIndex = word.indexOf('y');
  if (yIndex > 0) {
    return word.slice(0, yIndex);
  }
  
  // Get all consonants at start, treating 'th' as a cluster
  let i = 0;
  while (i < word.length && !isVowel(word[i]) && word[i] !== 'y') {
    // Handle 'th' as a single cluster
    if (word[i] === 't' && word[i+1] === 'h') {
      i += 2;
      break;
    }
    i++;
  }
  return word.slice(0, i);
};

export const translate = (text) => {
  return text.split(' ').map(word => {
    if (startsWithVowelLike(word)) {
      return word + 'ay';
    }
    
    const cluster = getConsonantCluster(word);
    const rest = word.slice(cluster.length);
    return rest + cluster + 'ay';
  }).join(' ');
};
