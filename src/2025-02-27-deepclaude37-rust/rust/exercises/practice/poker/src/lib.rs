use std::collections::HashMap;

fn parse_hand(hand: &str) -> Vec<(u8, char)> {
    let mut cards = hand.split_whitespace()
        .map(|card| {
            let (rank_str, suit) = if card.starts_with("10") {
                ("10", card.chars().nth(2).unwrap())
            } else {
                (
                    &card[0..card.len() - 1],
                    card.chars().last().unwrap(),
                )
            };
            let rank = match rank_str {
                "2" => 2,
                "3" => 3,
                "4" => 4,
                "5" => 5,
                "6" => 6,
                "7" => 7,
                "8" => 8,
                "9" => 9,
                "10" => 10,
                "J" => 11,
                "Q" => 12,
                "K" => 13,
                "A" => 14,
                _ => panic!("invalid rank"),
            };
            (rank, suit)
        })
        .collect::<Vec<_>>();
    
    // Sort by rank in descending order
    cards.sort_by(|a, b| b.0.cmp(&a.0));
    cards
}

fn evaluate_hand(hand: &str) -> Vec<u8> {
    let cards = parse_hand(hand);
    let ranks: Vec<u8> = cards.iter().map(|c| c.0).collect();
    let suits: Vec<char> = cards.iter().map(|c| c.1).collect();

    // Check for flush
    let is_flush = suits.windows(2).all(|w| w[0] == w[1]);

    // Check for straights
    let is_normal_straight = ranks.windows(2).all(|w| w[0] == w[1] + 1);
    let is_ace_low_straight = ranks == [14, 5, 4, 3, 2];
    let is_straight = is_normal_straight || is_ace_low_straight;
    let straight_high = if is_ace_low_straight { 5 } else { ranks[0] };

    // Count occurrences of each rank
    let mut counts = HashMap::new();
    for &r in &ranks {
        *counts.entry(r).or_insert(0) += 1;
    }
    
    // Sort by count (descending), then by rank (descending)
    let mut count_vec: Vec<(u8, u8)> = counts.into_iter().map(|(r, c)| (c, r)).collect();
    count_vec.sort_by(|a, b| b.0.cmp(&a.0).then(b.1.cmp(&a.1)));

    // Create a key that represents the hand's strength
    let mut key = Vec::new();

    if is_straight && is_flush {
        // Straight flush (8)
        key.push(8);
        key.push(straight_high);
    } else if count_vec[0].0 == 4 {
        // Four of a kind (7)
        key.push(7);
        key.push(count_vec[0].1);
        key.push(count_vec[1].1);
    } else if count_vec[0].0 == 3 && count_vec.get(1).map_or(false, |c| c.0 == 2) {
        // Full house (6)
        key.push(6);
        key.push(count_vec[0].1);
        key.push(count_vec[1].1);
    } else if is_flush {
        // Flush (5)
        key.push(5);
        key.extend_from_slice(&ranks);
    } else if is_straight {
        // Straight (4)
        key.push(4);
        key.push(straight_high);
    } else if count_vec[0].0 == 3 {
        // Three of a kind (3)
        key.push(3);
        key.push(count_vec[0].1);
        for &(_, r) in &count_vec[1..] {
            key.push(r);
        }
    } else if count_vec[0].0 == 2 && count_vec.get(1).map_or(false, |c| c.0 == 2) {
        // Two pair (2)
        key.push(2);
        key.push(count_vec[0].1);
        key.push(count_vec[1].1);
        key.push(count_vec[2].1);
    } else if count_vec[0].0 == 2 {
        // One pair (1)
        key.push(1);
        key.push(count_vec[0].1);
        for &(_, r) in &count_vec[1..] {
            key.push(r);
        }
    } else {
        // High card (0)
        key.push(0);
        key.extend_from_slice(&ranks);
    }

    key
}

/// Given a list of poker hands, return a list of those hands which win.
///
/// Note the type signature: this function should return _the same_ reference to
/// the winning hand(s) as were passed in, not reconstructed strings which happen to be equal.
pub fn winning_hands<'a>(hands: &[&'a str]) -> Vec<&'a str> {
    if hands.is_empty() {
        return Vec::new();
    }

    // Evaluate each hand and pair it with the original hand reference
    let mut hand_scores: Vec<(Vec<u8>, &'a str)> = hands
        .iter()
        .map(|&h| (evaluate_hand(h), h))
        .collect();

    // Find the maximum score
    hand_scores.sort_by(|a, b| b.0.cmp(&a.0));
    let max_score = hand_scores[0].0.clone();
    
    // Return all hands that have the maximum score
    hand_scores
        .into_iter()
        .filter(|(score, _)| score == &max_score)
        .map(|(_, hand)| hand)
        .collect()
}
