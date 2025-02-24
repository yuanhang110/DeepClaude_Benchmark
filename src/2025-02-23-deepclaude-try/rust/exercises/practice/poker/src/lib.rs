use std::collections::HashMap;

/// Given a list of poker hands, return a list of those hands which win.
///
/// Note the type signature: this function should return _the same_ reference to
/// the winning hand(s) as were passed in, not reconstructed strings which happen to be equal.
pub fn winning_hands<'a>(hands: &[&'a str]) -> Vec<&'a str> {
    let mut max_strength = Vec::new();
    let mut winners = Vec::new();
    
    for hand in hands.iter().copied() {
        let (ranks, suits) = parse_hand(hand);
        let strength = evaluate_strength(&ranks, &suits);
        
        if strength > max_strength {
            max_strength = strength;
            winners.clear();
            winners.push(hand);
        } else if strength == max_strength {
            winners.push(hand);
        }
    }
    
    winners
}

fn parse_hand(hand: &str) -> (Vec<u8>, Vec<char>) {
    let cards: Vec<&str> = hand.split_whitespace().collect();
    assert_eq!(cards.len(), 5, "Hand must have exactly 5 cards");
    
    let mut ranks = Vec::with_capacity(5);
    let mut suits = Vec::with_capacity(5);
    
    for card in cards {
        let (rank_str, suit) = card.split_at(card.len() - 1);
        let rank = match rank_str {
            "A" => 14,
            "K" => 13,
            "Q" => 12,
            "J" => 11,
            n => n.parse().unwrap_or_else(|_| panic!("Invalid rank")),
        };
        let suit_char = suit.chars().next().unwrap();
        ranks.push(rank);
        suits.push(suit_char);
    }
    
    ranks.sort_unstable_by(|a, b| b.cmp(a));
    (ranks, suits)
}

fn evaluate_strength(ranks: &[u8], suits: &[char]) -> Vec<u8> {
    let is_flush = suits.iter().all(|&s| s == suits[0]);
    let is_straight = is_straight(ranks);
    
    if is_flush && is_straight {
        let high = if ranks == &[14, 5, 4, 3, 2] { 5 } else { ranks[0] };
        return vec![8, high];  // Straight flush
    }
    
    let counts = get_counts(ranks);
    match counts[0].0 {
        4 => vec![7, counts[0].1, counts[1].1],  // Four of a kind
        3 if counts[1].0 == 2 => vec![6, counts[0].1, counts[1].1],  // Full house
        _ if is_flush => {
            let mut strength = vec![5];
            strength.extend_from_slice(ranks);
            strength
        }
        _ if is_straight => {
            let high = if ranks == &[14, 5, 4, 3, 2] { 5 } else { ranks[0] };
            vec![4, high]  // Straight
        }
        3 => {
            let mut strength = vec![3, counts[0].1];
            strength.extend(counts[1..].iter().map(|&(_, r)| r));
            strength  // Three of a kind
        }
        2 if counts[1].0 == 2 => {
            let (high_pair, low_pair) = if counts[0].1 > counts[1].1 {
                (counts[0].1, counts[1].1)
            } else {
                (counts[1].1, counts[0].1)
            };
            vec![2, high_pair, low_pair, counts[2].1]  // Two pair
        }
        2 => {
            let mut strength = vec![1, counts[0].1];
            strength.extend(counts[1..].iter().map(|&(_, r)| r));
            strength  // One pair
        }
        _ => {
            let mut strength = vec![0];
            strength.extend_from_slice(ranks);
            strength  // High card
        }
    }
}

fn is_straight(ranks: &[u8]) -> bool {
    (0..4).all(|i| ranks[i] == ranks[i + 1] + 1) || 
    ranks == &[14, 5, 4, 3, 2]  // Special case: Ace-low straight
}

fn get_counts(ranks: &[u8]) -> Vec<(u8, u8)> {
    let mut count_map = HashMap::new();
    for &rank in ranks {
        *count_map.entry(rank).or_insert(0) += 1;
    }
    
    let mut counts: Vec<_> = count_map.into_iter()
        .map(|(rank, count)| (count, rank))
        .collect();
    counts.sort_unstable_by(|a, b| b.0.cmp(&a.0).then(b.1.cmp(&a.1)));
    counts
}
