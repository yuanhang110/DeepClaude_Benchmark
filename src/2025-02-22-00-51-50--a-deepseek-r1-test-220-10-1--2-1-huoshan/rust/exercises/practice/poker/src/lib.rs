/// Given a list of poker hands, return a list of those hands which win.
///
/// Note the type signature: this function should return _the same_ reference to
/// the winning hand(s) as were passed in, not reconstructed strings which happen to be equal.
pub fn winning_hands<'a>(hands: &[&'a str]) -> Vec<&'a str> {
    #[derive(Debug)]
    struct HandScore<'a> {
        score: (u8, Vec<u8>),
        hand: &'a str,
    }

    let mut hand_scores = hands.iter().map(|&hand| {
        let cards = parse_hand(hand);
        let score = evaluate_hand(&cards);
        HandScore { score, hand }
    }).collect::<Vec<_>>();

    hand_scores.sort_by(|a, b| b.score.cmp(&a.score));

    let max_score = &hand_scores[0].score;
    hand_scores.iter()
        .take_while(|hs| hs.score == *max_score)
        .map(|hs| hs.hand)
        .collect()
}

fn parse_hand(hand: &str) -> Vec<(u8, char)> {
    hand.split_whitespace()
        .map(|card| {
            let (rank_str, suit) = card.split_at(card.len() - 1);
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
                _ => panic!("Invalid rank: {}", rank_str),
            };
            (rank, suit.chars().next().unwrap())
        })
        .collect()
}

fn evaluate_hand(hand: &[(u8, char)]) -> (u8, Vec<u8>) {
    let mut ranks: Vec<u8> = hand.iter().map(|(r, _)| *r).collect();
    ranks.sort_unstable();

    let is_flush = hand.windows(2).all(|w| w[0].1 == w[1].1);

    let is_straight = ranks.windows(2).all(|w| w[1] - w[0] == 1)
        || (ranks == vec![2, 3, 4, 5, 14]); // Ace-low straight

    let mut counts = std::collections::HashMap::new();
    for &r in &ranks {
        *counts.entry(r).or_insert(0) += 1;
    }
    let mut counts: Vec<_> = counts.into_iter().collect();
    counts.sort_by(|a, b| b.1.cmp(&a.1).then_with(|| b.0.cmp(&a.0)));

    let mut score_rank = match counts.as_slice() {
        [(r4, 4), (r1, 1)] => (7, vec![*r4, *r1]), // Four of a kind
        [(r3, 3), (r2, 2)] => (6, vec![*r3, *r2]), // Full house
        [(r3, 3), (r1, 1), (r2, 1)] => (3, vec![*r3, *r1.max(r2)]), // Three of a kind
        [(r2a, 2), (r2b, 2), (r1, 1)] => (2, vec![*r2a.max(r2b), *r2a.min(r2b), *r1]), // Two pair
        [(r2, 2), (r1a, 1), (r1b, 1), (r1c, 1)] => {
            let mut kickers = vec![*r1a, *r1b, *r1c];
            kickers.sort_unstable_by(|a, b| b.cmp(a));
            (1, vec![*r2].into_iter().chain(kickers).collect())
        }, // One pair
        _ => (0, ranks.iter().rev().cloned().collect()), // High card
    };

    // Check for straight/flush combinations
    if is_straight && is_flush {
        score_rank = if ranks == vec![2, 3, 4, 5, 14] {
            (8, vec![5, 4, 3, 2, 1]) // Ace-low straight flush
        } else {
            (8, vec![ranks[4], ranks[3], ranks[2], ranks[1], ranks[0]]) // Straight flush
        };
    } else if is_flush {
        score_rank = (5, ranks.iter().rev().cloned().collect());
    } else if is_straight {
        score_rank = if ranks == vec![2, 3, 4, 5, 14] {
            (4, vec![5, 4, 3, 2, 1]) // Ace-low straight
        } else {
            (4, vec![ranks[4], ranks[3], ranks[2], ranks[1], ranks[0]])
        };
    }

    score_rank
}
