// Card values mapping
const VALUES = {
  '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9,
  '10': 10, 'J': 11, 'Q': 12, 'K': 13, 'A': 14
};

// Hand rankings from highest to lowest
const RANKS = {
  STRAIGHT_FLUSH: 9,
  FOUR_OF_KIND: 8,
  FULL_HOUSE: 7,
  FLUSH: 6,
  STRAIGHT: 5,
  THREE_OF_KIND: 4,
  TWO_PAIR: 3,
  ONE_PAIR: 2,
  HIGH_CARD: 1
};

// Parse a single card into value and suit
const parseCard = (card) => ({
  value: VALUES[card.slice(0, -1)],
  suit: card.slice(-1)
});

// Parse a hand string into array of card objects
const parseHand = (hand) => {
  const cards = hand.split(' ').map(parseCard);
  const values = cards.map(card => card.value).sort((a, b) => b - a);
  const suits = cards.map(card => card.suit);
  return { cards, values, suits, original: hand };
};

// Check if values form a straight
const isStraight = (values) => {
  // Handle ace-low straight (A-2-3-4-5)
  if (values.join(',') === '14,5,4,3,2') return { isStraight: true, highCard: 5 };
  
  // Check for regular straight
  for (let i = 1; i < values.length; i++) {
    if (values[i-1] !== values[i] + 1) return { isStraight: false };
  }
  return { isStraight: true, highCard: values[0] };
};

// Check if all cards are the same suit
const isFlush = (suits) => suits.every(suit => suit === suits[0]);

// Get frequency map of values
const getValueFrequencies = (values) => {
  const freq = new Map();
  values.forEach(value => freq.set(value, (freq.get(value) || 0) + 1));
  return Array.from(freq.entries())
    .sort(([v1, f1], [v2, f2]) => f2 - f1 || v2 - v1);
};

// Evaluate a hand and return its rank and comparison key
const evaluateHand = ({ values, suits }) => {
  const flush = isFlush(suits);
  const straight = isStraight(values);
  const frequencies = getValueFrequencies(values);

  // Straight flush
  if (flush && straight.isStraight) {
    return { rank: RANKS.STRAIGHT_FLUSH, key: [straight.highCard] };
  }

  // Four of a kind
  if (frequencies[0][1] === 4) {
    return { 
      rank: RANKS.FOUR_OF_KIND, 
      key: [frequencies[0][0], frequencies[1][0]]
    };
  }

  // Full house
  if (frequencies[0][1] === 3 && frequencies[1][1] === 2) {
    return { 
      rank: RANKS.FULL_HOUSE, 
      key: [frequencies[0][0], frequencies[1][0]]
    };
  }

  // Flush
  if (flush) {
    return { rank: RANKS.FLUSH, key: values };
  }

  // Straight
  if (straight.isStraight) {
    return { rank: RANKS.STRAIGHT, key: [straight.highCard] };
  }

  // Three of a kind
  if (frequencies[0][1] === 3) {
    return { 
      rank: RANKS.THREE_OF_KIND,
      key: [frequencies[0][0], ...values.filter(v => v !== frequencies[0][0])]
    };
  }

  // Two pair
  if (frequencies[0][1] === 2 && frequencies[1][1] === 2) {
    return { 
      rank: RANKS.TWO_PAIR,
      key: [
        Math.max(frequencies[0][0], frequencies[1][0]),
        Math.min(frequencies[0][0], frequencies[1][0]),
        frequencies[2][0]
      ]
    };
  }

  // One pair
  if (frequencies[0][1] === 2) {
    return { 
      rank: RANKS.ONE_PAIR,
      key: [frequencies[0][0], ...values.filter(v => v !== frequencies[0][0])]
    };
  }

  // High card
  return { rank: RANKS.HIGH_CARD, key: values };
};

// Compare two hands based on rank and key
const compareHands = (hand1, hand2) => {
  const eval1 = evaluateHand(hand1);
  const eval2 = evaluateHand(hand2);

  if (eval1.rank !== eval2.rank) {
    return eval1.rank - eval2.rank;
  }

  for (let i = 0; i < eval1.key.length; i++) {
    if (eval1.key[i] !== eval2.key[i]) {
      return eval1.key[i] - eval2.key[i];
    }
  }
  return 0;
};

export const bestHands = (hands) => {
  if (!hands || hands.length === 0) return [];
  
  const parsedHands = hands.map(parseHand);
  const bestHand = parsedHands.reduce((best, current) => 
    compareHands(current, best) > 0 ? current : best
  );

  return parsedHands
    .filter(hand => compareHands(hand, bestHand) === 0)
    .map(hand => hand.original);
};
