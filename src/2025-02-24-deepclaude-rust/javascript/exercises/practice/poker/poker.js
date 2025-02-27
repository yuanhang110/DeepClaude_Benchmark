//
// This is only a SKELETON file for the 'Poker' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const bestHands = (hands) => {
  if (hands.length === 0) return [];

  const evaluateHand = (hand) => {
    const cards = hand.split(' ');
    const rankOrder = { '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9, 'T': 10, 'J': 11, 'Q': 12, 'K': 13, 'A': 14 };
    const suits = [];
    const ranks = [];
    for (const card of cards) {
      let rankChar;
      let suit;
      if (card.startsWith('10')) {
        rankChar = 'T';
        suit = card[2];
      } else {
        rankChar = card[0];
        suit = card[1];
      }
      suits.push(suit);
      ranks.push(rankOrder[rankChar]);
    }
    ranks.sort((a, b) => a - b);

    const isFlush = suits.every(s => s === suits[0]);
    const isStraight = (() => {
      if (ranks[4] - ranks[0] === 4 && new Set(ranks).size === 5) return true;
      if (ranks[0] === 2 && ranks[1] === 3 && ranks[2] === 4 && ranks[3] === 5 && ranks[4] === 14) return true;
      return false;
    })();

    const counts = {};
    for (const rank of ranks) {
      counts[rank] = (counts[rank] || 0) + 1;
    }
    const countValues = Object.values(counts).sort((a, b) => b - a);

    let rank, values = [];
    if (isStraight && isFlush) {
      rank = 8;
      const highCard = (ranks[0] === 2 && ranks[4] === 14) ? 5 : ranks[4];
      values = [highCard];
    } else if (countValues[0] === 4) {
      rank = 7;
      const quadRank = parseInt(Object.keys(counts).find(k => counts[k] === 4));
      const kicker = parseInt(Object.keys(counts).find(k => counts[k] === 1));
      values = [quadRank, kicker];
    } else if (countValues[0] === 3 && countValues[1] === 2) {
      rank = 6;
      const triplet = parseInt(Object.keys(counts).find(k => counts[k] === 3));
      const pair = parseInt(Object.keys(counts).find(k => counts[k] === 2));
      values = [triplet, pair];
    } else if (isFlush) {
      rank = 5;
      values = [...ranks].sort((a, b) => b - a);
    } else if (isStraight) {
      rank = 4;
      const highCard = (ranks[0] === 2 && ranks[4] === 14) ? 5 : ranks[4];
      values = [highCard];
    } else if (countValues[0] === 3) {
      rank = 3;
      const triplet = parseInt(Object.keys(counts).find(k => counts[k] === 3));
      const kickers = ranks.filter(r => r !== triplet).sort((a, b) => b - a);
      values = [triplet, ...kickers];
    } else if (countValues[0] === 2 && countValues[1] === 2) {
      rank = 2;
      const pairs = Object.entries(counts)
        .filter(([k, v]) => v === 2)
        .map(([k, v]) => parseInt(k))
        .sort((a, b) => b - a);
      const kicker = parseInt(Object.entries(counts).find(([k, v]) => v === 1)[0]);
      values = [...pairs, kicker];
    } else if (countValues[0] === 2) {
      rank = 1;
      const pairRank = parseInt(Object.keys(counts).find(k => counts[k] === 2));
      const kickers = ranks.filter(r => r !== pairRank).sort((a, b) => b - a);
      values = [pairRank, ...kickers];
    } else {
      rank = 0;
      values = [...ranks].sort((a, b) => b - a);
    }

    return { hand, key: [rank, ...values] };
  };

  const compareKeys = (a, b) => {
    for (let i = 0; i < Math.max(a.length, b.length); i++) {
      const aVal = a[i] || 0;
      const bVal = b[i] || 0;
      if (aVal > bVal) return 1;
      if (aVal < bVal) return -1;
    }
    return 0;
  };

  const evaluated = hands.map(evaluateHand);
  let maxKey = evaluated[0].key;
  evaluated.forEach(ev => {
    if (compareKeys(ev.key, maxKey) > 0) {
      maxKey = ev.key;
    }
  });

  const best = evaluated.filter(ev => compareKeys(ev.key, maxKey) === 0).map(ev => ev.hand);
  return best;
};
