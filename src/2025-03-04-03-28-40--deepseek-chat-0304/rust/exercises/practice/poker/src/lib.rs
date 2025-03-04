use std::cmp::Ordering;
use std::collections::HashMap;

#[derive(Debug, Clone, Copy, PartialEq, Eq, PartialOrd, Ord, Hash)]
enum Rank {
    Two = 2,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Jack,
    Queen,
    King,
    Ace,
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
enum Suit {
    Clubs,
    Diamonds,
    Hearts,
    Spades,
}

#[derive(Debug, PartialEq, Eq)]
struct Card {
    rank: Rank,
    suit: Suit,
}

#[derive(Debug, PartialEq, Eq, PartialOrd, Ord)]
enum HandRank {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    Straight,
    Flush,
    FullHouse,
    FourOfAKind,
    StraightFlush,
}

#[derive(Debug, Eq)]
struct Hand {
    cards: Vec<Card>,
    counts: HashMap<Rank, u8>,
    ranks: Vec<Rank>,
    hand_rank: HandRank,
}

impl Hand {
    fn new(cards: Vec<Card>) -> Self {
        let mut counts = HashMap::new();
        let mut ranks = cards.iter().map(|c| c.rank).collect::<Vec<_>>();
        ranks.sort_by(|a, b| b.cmp(a));

        for &rank in &ranks {
            *counts.entry(rank).or_insert(0) += 1;
        }

        let hand_rank = Self::evaluate_hand(&cards, &counts, &ranks);

        Hand {
            cards,
            counts,
            ranks,
            hand_rank,
        }
    }

    fn evaluate_hand(cards: &[Card], counts: &HashMap<Rank, u8>, ranks: &[Rank]) -> HandRank {
        let is_flush = cards.iter().all(|c| c.suit == cards[0].suit);
        let is_straight = Self::is_straight(ranks);

        if is_straight && is_flush {
            return HandRank::StraightFlush;
        }
        if counts.values().any(|&v| v == 4) {
            return HandRank::FourOfAKind;
        }
        if counts.values().filter(|&&v| v == 3).count() == 1 
            && counts.values().filter(|&&v| v == 2).count() == 1 {
            return HandRank::FullHouse;
        }
        if is_flush {
            return HandRank::Flush;
        }
        if is_straight {
            return HandRank::Straight;
        }
        if counts.values().any(|&v| v == 3) {
            return HandRank::ThreeOfAKind;
        }
        if counts.values().filter(|&&v| v == 2).count() == 2 {
            return HandRank::TwoPair;
        }
        if counts.values().any(|&v| v == 2) {
            return HandRank::OnePair;
        }
        HandRank::HighCard
    }

    fn is_straight(ranks: &[Rank]) -> bool {
        // Check for normal straight
        if ranks.windows(2).all(|w| w[0] as u8 == w[1] as u8 + 1) {
            return true;
        }
        // Check for low straight (A-2-3-4-5)
        ranks == &[Rank::Ace, Rank::Five, Rank::Four, Rank::Three, Rank::Two]
    }
}

impl PartialEq for Hand {
    fn eq(&self, other: &Self) -> bool {
        self.hand_rank == other.hand_rank && self.ranks == other.ranks
    }
}

impl Ord for Hand {
    fn cmp(&self, other: &Self) -> Ordering {
        match self.hand_rank.cmp(&other.hand_rank) {
            Ordering::Equal => self.ranks.cmp(&other.ranks),
            ord => ord,
        }
    }
}

impl PartialOrd for Hand {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

fn parse_card(s: &str) -> Card {
    let (rank_str, suit_str) = s.split_at(s.len() - 1);
    let rank = match rank_str {
        "2" => Rank::Two,
        "3" => Rank::Three,
        "4" => Rank::Four,
        "5" => Rank::Five,
        "6" => Rank::Six,
        "7" => Rank::Seven,
        "8" => Rank::Eight,
        "9" => Rank::Nine,
        "10" => Rank::Ten,
        "J" => Rank::Jack,
        "Q" => Rank::Queen,
        "K" => Rank::King,
        "A" => Rank::Ace,
        _ => panic!("Invalid rank"),
    };
    let suit = match suit_str {
        "C" => Suit::Clubs,
        "D" => Suit::Diamonds,
        "H" => Suit::Hearts,
        "S" => Suit::Spades,
        _ => panic!("Invalid suit"),
    };
    Card { rank, suit }
}

/// Given a list of poker hands, return a list of those hands which win.
///
/// Note the type signature: this function should return _the same_ reference to
/// the winning hand(s) as were passed in, not reconstructed strings which happen to be equal.
pub fn winning_hands<'a>(hands: &[&'a str]) -> Vec<&'a str> {
    let parsed_hands: Vec<Hand> = hands
        .iter()
        .map(|&hand| {
            let cards = hand.split_whitespace().map(parse_card).collect();
            Hand::new(cards)
        })
        .collect();

    let max_hand = parsed_hands.iter().max().unwrap();
    
    hands
        .iter()
        .zip(parsed_hands.iter())
        .filter(|(_, hand)| *hand == max_hand)
        .map(|(&original, _)| original)
        .collect()
}
