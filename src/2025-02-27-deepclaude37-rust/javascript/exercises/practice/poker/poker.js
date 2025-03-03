//
// This is only a SKELETON file for the 'Poker' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const bestHands = (hands) => {
  // Helper function to parse a card string into an object with rank and suit
  const parseCard = (card) => {
    const valuePart = card.slice(0, -1);
    const suit = card.slice(-1);
    let rank;
    switch (valuePart) {
      case 'J': rank = 11; break;
      case 'Q': rank = 12; break;
      case 'K': rank = 13; break;
      case 'A': rank = 14; break;
      default: rank = parseInt(valuePart, 10);
    }
    return { rank, suit };
  };

  // Helper function to evaluate a hand and return its type and rank values
  const evaluateHand = (cards) => {
    const ranks = cards.map(c => c.rank).sort((a, b) => b - a);
    const suits = cards.map(c => c.suit);
    
    // Check if hand is a flush (all same suit)
    const isFlush = suits.every(s => s === suits[0]);
    
    // Check if hand is a straight
    let isStraight = false;
    let straightHigh = ranks[0];
    
    // Check normal straight (all consecutive ranks)
    if (new Set(ranks).size === 5) {
      if (ranks[0] - ranks[4] === 4) {
        isStraight = true;
      } 
      // Check for A-5-4-3-2 straight (Ace low)
      else if (ranks[0] === 14 && ranks[1] === 5 && ranks[2] === 4 && 
               ranks[3] === 3 && ranks[4] === 2) {
        isStraight = true;
        straightHigh = 5; // High card is 5 in A-5-4-3-2 straight
      }
    }
    
    // Count occurrences of each rank
    const rankCounts = {};
    ranks.forEach(r => rankCounts[r] = (rankCounts[r] || 0) + 1);
    
    // Sort ranks by count (highest first), then by rank value (highest first)
    const groupedRanks = Object.keys(rankCounts)
      .map(Number)
      .sort((a, b) => rankCounts[b] - rankCounts[a] || b - a);
    
    // Hand types with scores from highest (9) to lowest (1)
    if (isFlush && isStraight) {
      return { type: 9, ranks: [straightHigh] }; // Straight flush
    }
    
    if (rankCounts[groupedRanks[0]] === 4) {
      return { type: 8, ranks: [groupedRanks[0], groupedRanks[1]] }; // Four of a kind
    }
    
    if (rankCounts[groupedRanks[0]] === 3 && rankCounts[groupedRanks[1]] === 2) {
      return { type: 7, ranks: [groupedRanks[0], groupedRanks[1]] }; // Full house
    }
    
    if (isFlush) {
      return { type: 6, ranks }; // Flush
    }
    
    if (isStraight) {
      return { type: 5, ranks: [straightHigh] }; // Straight
    }
    
    if (rankCounts[groupedRanks[0]] === 3) {
      return { type: 4, ranks: groupedRanks }; // Three of a kind
    }
    
    if (rankCounts[groupedRanks[0]] === 2 && rankCounts[groupedRanks[1]] === 2) {
      return { type: 3, ranks: groupedRanks }; // Two pair
    }
    
    if (rankCounts[groupedRanks[0]] === 2) {
      return { type: 2, ranks: groupedRanks }; // One pair
    }
    
    return { type: 1, ranks }; // High card
  };

  // Compare two scores lexicographically
  const compareScores = (a, b) => {
    for (let i = 0; i < Math.min(a.length, b.length); i++) {
      if (a[i] !== b[i]) return a[i] - b[i];
    }
    return a.length - b.length;
  };

  // Parse and evaluate each hand
  const evaluatedHands = hands.map(hand => {
    const cards = hand.split(' ').map(parseCard);
    const evaluation = evaluateHand(cards);
    
    return {
      hand,
      score: [evaluation.type, ...evaluation.ranks]
    };
  });
  
  // Find the maximum score
  let bestScore = evaluatedHands[0].score;
  
  for (const evaluated of evaluatedHands) {
    if (compareScores(evaluated.score, bestScore) > 0) {
      bestScore = evaluated.score;
    }
  }
  
  // Return all hands that have the best score
  return evaluatedHands
    .filter(evaluated => compareScores(evaluated.score, bestScore) === 0)
    .map(evaluated => evaluated.hand);
};
