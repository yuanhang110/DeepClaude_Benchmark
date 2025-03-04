// Helper functions
const getRankValue = (rank) => {
  const ranks = '23456789TJQKA';
  return ranks.indexOf(rank);
};

const getHandDetails = (hand) => {
  const cards = hand.split(' ');
  const faces = cards.map(card => card[0]);
  const suits = cards.map(card => card[1]);
  
  // Count duplicates and get face values
  const counts = faces.reduce((acc, face) => {
    acc[face] = (acc[face] || 0) + 1;
    return acc;
  }, {});
  
  // Sort faces by count first, then by rank
  const sortedFaces = Object.keys(counts).sort((a, b) => {
    const countDiff = counts[b] - counts[a];
    if (countDiff !== 0) return countDiff;
    return getRankValue(b) - getRankValue(a);
  });
  
  // Get all face values sorted by rank for tie-breakers
  const faceValues = faces.map(face => getRankValue(face)).sort((a, b) => b - a);
  
  // Check for flush
  const flush = suits.every(suit => suit === suits[0]);
  
  // Check for straight
  const uniqueSorted = [...new Set(faceValues)].sort((a, b) => b - a);
  const isStraight = uniqueSorted.length === 5 && 
    (uniqueSorted[0] - uniqueSorted[4] === 4 || 
     (uniqueSorted.includes(12) && uniqueSorted.includes(3))); // Handle A-2-3-4-5
  
  return {
    counts,
    sortedFaces,
    faceValues,
    flush,
    straight: isStraight,
    isLowStraight: isStraight && uniqueSorted.includes(3) // A-2-3-4-5
  };
};

const getHandStrength = (handDetails) => {
  const { counts, sortedFaces, faceValues, flush, straight, isLowStraight } = handDetails;
  
  // Get counts in order
  const countValues = Object.values(counts).sort((a, b) => b - a);
  
  // Adjust for low straight
  const adjustedValues = isLowStraight ? [3, 2, 1, 0, 12] : faceValues;
  
  if (flush && straight) {
    return { strength: 8, values: adjustedValues }; // Straight flush
  }
  if (countValues[0] === 4) {
    return { strength: 7, values: [getRankValue(sortedFaces[0]), ...adjustedValues] }; // Four of a kind
  }
  if (countValues[0] === 3 && countValues[1] === 2) {
    return { strength: 6, values: [getRankValue(sortedFaces[0]), getRankValue(sortedFaces[1])] }; // Full house
  }
  if (flush) {
    return { strength: 5, values: adjustedValues }; // Flush
  }
  if (straight) {
    return { strength: 4, values: adjustedValues }; // Straight
  }
  if (countValues[0] === 3) {
    return { strength: 3, values: [getRankValue(sortedFaces[0]), ...adjustedValues] }; // Three of a kind
  }
  if (countValues[0] === 2 && countValues[1] === 2) {
    return { 
      strength: 2, 
      values: [
        getRankValue(sortedFaces[0]), 
        getRankValue(sortedFaces[1]),
        ...adjustedValues.filter(v => v !== getRankValue(sortedFaces[0]) && v !== getRankValue(sortedFaces[1]))
      ]
    }; // Two pair
  }
  if (countValues[0] === 2) {
    return { 
      strength: 1, 
      values: [
        getRankValue(sortedFaces[0]),
        ...adjustedValues.filter(v => v !== getRankValue(sortedFaces[0]))
      ]
    }; // One pair
  }
  return { strength: 0, values: adjustedValues }; // High card
};

const compareHands = (hand1, hand2) => {
  const details1 = getHandDetails(hand1);
  const details2 = getHandDetails(hand2);
  const strength1 = getHandStrength(details1);
  const strength2 = getHandStrength(details2);
  
  // Compare hand strengths
  if (strength1.strength !== strength2.strength) {
    return strength2.strength - strength1.strength;
  }
  
  // Compare values for tie-breakers
  for (let i = 0; i < strength1.values.length; i++) {
    if (strength1.values[i] !== strength2.values[i]) {
      return strength2.values[i] - strength1.values[i];
    }
  }
  
  return 0;
};

export const bestHands = (hands) => {
  if (hands.length === 1) return hands;
  
  let best = [hands[0]];
  for (let i = 1; i < hands.length; i++) {
    const comparison = compareHands(best[0], hands[i]);
    if (comparison < 0) {
      best = [hands[i]];
    } else if (comparison === 0) {
      best.push(hands[i]);
    }
  }
  
  return best;
};
