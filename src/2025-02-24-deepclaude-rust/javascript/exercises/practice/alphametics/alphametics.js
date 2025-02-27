//
// This is only a SKELETON file for the 'Alphametics' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const solve = (puzzle) => {
  const equation = puzzle.split('=');
  const leftParts = equation[0].split('+').map(part => part.trim());
  const rightPart = equation[1].trim();
  const allWords = [...leftParts, rightPart];

  // Collect all unique letters and their positions/frequencies
  const letters = new Set();
  const letterPositions = new Map();
  const letterFrequencies = new Map();

  for (const word of allWords) {
    for (let i = 0; i < word.length; i++) {
      const c = word[i];
      letters.add(c);
      
      // Track highest position (length - index for leftmost priority)
      const pos = word.length - i;
      letterPositions.set(c, Math.max(pos, letterPositions.get(c) || 0));
      
      // Track frequency
      letterFrequencies.set(c, (letterFrequencies.get(c) || 0) + 1);
    }
  }

  // Collect leading letters
  const leadingLetters = new Set();
  for (const word of allWords) {
    if (word.length > 0) {
      leadingLetters.add(word[0]);
    }
  }

  const uniqueLetters = Array.from(letters);
  
  // Sort letters by leading status, position, and frequency
  uniqueLetters.sort((a, b) => {
    const aLead = leadingLetters.has(a);
    const bLead = leadingLetters.has(b);
    
    if (aLead !== bLead) {
      return aLead ? -1 : 1;
    }
    
    const posA = letterPositions.get(a);
    const posB = letterPositions.get(b);
    if (posA !== posB) {
      return posB - posA;  // Higher positions first
    }
    
    const freqA = letterFrequencies.get(a);
    const freqB = letterFrequencies.get(b);
    return freqB - freqA;  // Higher frequencies first
  });

  // Function to convert a word to its numerical value based on assignment
  const wordToNumber = (word, assign) => {
    let num = 0;
    for (const c of word) {
      num = num * 10 + assign[c];
    }
    return num;
  };

  function* generateAssignments() {
    const usedDigits = new Set();
    const assignment = {};

    function* backtrack(index) {
      if (index === uniqueLetters.length) {
        yield { ...assignment };
        return;
      }
      const currentLetter = uniqueLetters[index];
      const start = leadingLetters.has(currentLetter) ? 1 : 0;
      for (let d = start; d <= 9; d++) {
        if (!usedDigits.has(d)) {
          usedDigits.add(d);
          assignment[currentLetter] = d;
          yield* backtrack(index + 1);
          usedDigits.delete(d);
          delete assignment[currentLetter];
        }
      }
    }

    yield* backtrack(0);
  }

  for (const assign of generateAssignments()) {
    const sumLeft = leftParts.reduce((acc, word) => acc + wordToNumber(word, assign), 0);
    const sumRight = wordToNumber(rightPart, assign);
    if (sumLeft === sumRight) {
      return assign;
    }
  }

  return null;
};
