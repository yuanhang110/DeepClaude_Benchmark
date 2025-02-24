const cardRank = {
  '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, 
  '8': 8, '9': 9, '10': 10, 'J': 11, 'Q': 12, 'K': 13, 'A': 14
};

function parseHand(hand) {
  return hand.split(' ').map(card => {
    const rank = card.slice(0, -1);
    return { 
      rank: cardRank[rank], 
      suit: card.slice(-1),
      original: card 
    };
  });
}

function evaluateHand(cards) {
  const ranks = cards.map(c => c.rank).sort((a, b) => b - a);
  const suits = cards.map(c => c.suit);
  const countByRank = ranks.reduce((acc, r) => ({...acc, [r]: (acc[r] || 0) + 1}), {});
  const groups = Object.entries(countByRank).sort((a, b) => b[1] - a[1] || b[0] - a[0]);
  
  const isFlush = new Set(suits).size === 1;
  const isStraight = new Set(ranks).size === 5 && Math.max(...ranks) - Math.min(...ranks) === 4;
  const isLowStraight = ranks.join(',') === '14,5,4,3,2';
  
  // Check straight flush (including royal flush)
  if (isStraight && isFlush) {
    return [8, Math.max(...ranks)];
  }
  if (isLowStraight && isFlush) {
    return [8, 5]; // Low straight (5-high)
  }
  
  // Check four of a kind
  if (groups[0][1] === 4) {
    return [7, Number(groups[0][0]), Number(groups[1][0])];
  }
  
  // Check full house
  if (groups[0][1] === 3 && groups[1][1] === 2) {
    return [6, Number(groups[0][0]), Number(groups[1][0])];
  }
  
  // Check flush
  if (isFlush) {
    return [5, ...ranks];
  }
  
  // Check straight
  if (isStraight) {
    return [4, Math.max(...ranks)];
  }
  if (isLowStraight) {
    return [4, 5]; // Low straight (5-high)
  }
  
  // Check three of a kind
  if (groups[0][1] === 3) {
    return [3, Number(groups[0][0]), ...groups.slice(1).map(g => Number(g[0]))];
  }
  
  // Check two pair
  if (groups[0][1] === 2 && groups[1][1] === 2) {
    return [2, Number(groups[0][0]), Number(groups[1][0]), Number(groups[2][0])];
  }
  
  // Check one pair
  if (groups[0][1] === 2) {
    return [1, Number(groups[0][0]), ...groups.slice(1).map(g => Number(g[0]))];
  }
  
  // High card
  return [0, ...ranks];
}

export const bestHands = (hands) => {
  if (hands.length === 1) return hands;
  
  const evaluated = hands.map(hand => {
    const cards = parseHand(hand);
    return {
      original: hand,
      score: evaluateHand(cards)
    };
  });
  
  // Compare scores element-wise with numeric comparison
  const maxScore = evaluated.reduce((maxEntry, currEntry) => {
    const curr = currEntry.score;
    const max = maxEntry.score;
    for (let i = 0; i < Math.max(curr.length, max.length); i++) {
      const currVal = curr[i] || 0;
      const maxVal = max[i] || 0;
      if (currVal > maxVal) return currEntry;
      if (currVal < maxVal) return maxEntry;
    }
    return maxEntry; // If all elements are equal
  }, evaluated[0]).score;
  
  return evaluated
    .filter(e => e.score.every((val, i) => val === (maxScore[i] || 0)))
    .map(e => e.original);
};
