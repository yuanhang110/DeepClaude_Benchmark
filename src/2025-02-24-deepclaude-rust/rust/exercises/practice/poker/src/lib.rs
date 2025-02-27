/// Given a list of poker hands, return a list of those hands which win.
///
/// Note the type signature: this function should return _the same_ reference to
/// the winning hand(s) as were passed in, not reconstructed strings which happen to be equal.
pub fn winning_hands<'a>(hands: &[&'a str]) -> Vec<&'a str> {
    if hands.is_empty() {
        return Vec::new();
    }

    let mut hand_scores = Vec::new();
    for &hand in hands {
        let cards = parse_hand(hand);
        let score = evaluate_hand(&cards);
        hand_scores.push((score, hand));
    }

    let max_score = hand_scores.iter().map(|(s, _)| s.clone()).max().unwrap();
    hand_scores.into_iter()
        .filter(|(s, _)| s == &max_score)
        .map(|(_, h)| h)
        .collect()
}

fn parse_hand(hand: &str) -> Vec<(u8, char)> {
    hand.split_whitespace()
        .map(|card| {
            let (rank_str, suit) = card.split_at(card.len() - 1);
            let rank = match rank_str {
                "A" => 14,
                "K" => 13,
                "Q" => 12,
                "J" => 11,
                "T" => 10,
                _ => rank_str.parse().unwrap_or_else(|_| panic!("Invalid rank: {}", rank_str)),
            };
            (rank, suit.chars().next().unwrap())
        })
        .collect()
}

fn evaluate_hand(cards: &[(u8, char)]) -> (u8, Vec<u8>) {
    let mut ranks = cards.iter().map(|c| c.0).collect::<Vec<_>>();
    ranks.sort_unstable_by(|a, b| b.cmp(a));
    let mut suits = cards.iter().map(|c| c.1).collect::<Vec<_>>();
    suits.sort_unstable();
    let is_flush = suits.windows(2).all(|w| w[0] == w[1]);
    let is_straight = ranks.windows(2).all(|w| w[0] - w[1] == 1) || (ranks == vec![14, 5, 4, 3, 2]);

    let rank_counts = {
        let mut counts = std::collections::HashMap::new();
        for &r in &ranks {
            *counts.entry(r).or_insert(0) += 1;
        }
        let mut counts: Vec<_> = counts.into_iter().map(|(r, c)| (c, r)).collect();
        counts.sort_unstable_by(|a, b| b.cmp(a));
        counts
    };

    let adjusted_ranks = if is_straight && ranks == vec![14, 5, 4, 3, 2] {
        vec![5, 4, 3, 2, 1]
    } else {
        ranks.clone()
    };

    if is_flush && is_straight {
        (8, adjusted_ranks)
    } else if rank_counts[0].0 == 4 {
        let mut kickers = ranks.clone();
        kickers.retain(|&r| r != rank_counts[0].1);
        (7, vec![rank_counts[0].1, kickers[0]])
    } else if rank_counts[0].0 == 3 && rank_counts[1].0 == 2 {
        (6, vec![rank_counts[0].1, rank_counts[1].1])
    } else if is_flush {
        (5, adjusted_ranks)
    } else if is_straight {
        (4, adjusted_ranks)
    } else if rank_counts[0].0 == 3 {
        let mut kickers = ranks.clone();
        kickers.retain(|&r| r != rank_counts[0].1);
        (3, vec![rank_counts[0].1, kickers[0], kickers[1]])
    } else if rank_counts[0].0 == 2 && rank_counts[1].0 == 2 {
        let pair1 = rank_counts[0].1;
        let pair2 = rank_counts[1].1;
        let mut kickers = ranks.clone();
        kickers.retain(|&r| r != pair1 && r != pair2);
        (2, vec![pair1.max(pair2), pair1.min(pair2), kickers[0]])
    } else if rank_counts[0].0 == 2 {
        let pair = rank_counts[0].1;
        let mut kickers = ranks.clone();
        kickers.retain(|&r| r != pair);
        (1, vec![pair, kickers[0], kickers[1], kickers[2]])
    } else {
        (0, adjusted_ranks)
    }
}
