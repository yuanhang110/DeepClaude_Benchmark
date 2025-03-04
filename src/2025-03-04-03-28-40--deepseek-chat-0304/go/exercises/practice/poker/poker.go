package poker

import (
	"errors"
	"sort"
	"strings"
)

type Card struct {
	rank int
	suit byte
}

type Hand struct {
	cards    []Card
	original string
	strength int
	values   []int
}

var rankValues = map[byte]int{
	'2': 2, '3': 3, '4': 4, '5': 5,
	'6': 6, '7': 7, '8': 8, '9': 9,
	'T': 10, 'J': 11, 'Q': 12, 'K': 13, 'A': 14,
	'♡': 0, '♢': 0, '♤': 0, '♧': 0, // Handle suit symbols
}

func parseCard(card string) (Card, error) {
	if len(card) < 2 {
		return Card{}, errors.New("invalid card")
	}
	rank := card[0]
	suit := card[1]
	value, ok := rankValues[rank]
	if !ok {
		return Card{}, errors.New("invalid rank")
	}
	return Card{value, suit}, nil
}

func parseHand(hand string) (Hand, error) {
	cards := strings.Split(hand, " ")
	if len(cards) != 5 {
		return Hand{}, errors.New("hand must have 5 cards")
	}

	var parsed []Card
	for _, card := range cards {
		c, err := parseCard(card)
		if err != nil {
			return Hand{}, err
		}
		parsed = append(parsed, c)
	}

	sort.Slice(parsed, func(i, j int) bool {
		return parsed[i].rank > parsed[j].rank
	})

	h := Hand{
		cards:    parsed,
		original: hand,
	}
	h.evaluate()
	return h, nil
}

func (h *Hand) evaluate() {
	values := make([]int, 15) // Need index 14 for Ace
	for _, card := range h.cards {
		values[card.rank]++
	}

	pairs := 0
	three := false
	four := false
	for _, count := range values {
		if count == 2 {
			pairs++
		} else if count == 3 {
			three = true
		} else if count == 4 {
			four = true
		}
	}

	flush := true
	firstSuit := h.cards[0].suit
	for _, card := range h.cards {
		if card.suit != firstSuit {
			flush = false
			break
		}
	}

	straight := true
	for i := 1; i < 5; i++ {
		if h.cards[i].rank != h.cards[i-1].rank-1 {
			straight = false
			break
		}
	}

	if straight && flush && h.cards[0].rank == 14 {
		h.strength = 10 // Royal flush
	} else if straight && flush {
		h.strength = 9 // Straight flush
	} else if four {
		h.strength = 8 // Four of a kind
	} else if three && pairs == 1 {
		h.strength = 7 // Full house
	} else if flush {
		h.strength = 6 // Flush
	} else if straight {
		h.strength = 5 // Straight
	} else if three {
		h.strength = 4 // Three of a kind
	} else if pairs == 2 {
		h.strength = 3 // Two pairs
	} else if pairs == 1 {
		h.strength = 2 // One pair
	} else {
		h.strength = 1 // High card
	}

	h.values = make([]int, 5)
	for i, card := range h.cards {
		h.values[i] = card.rank
	}
}

func compareHands(a, b Hand) bool {
	if a.strength != b.strength {
		return a.strength > b.strength
	}
	for i := 0; i < 5; i++ {
		if a.values[i] != b.values[i] {
			return a.values[i] > b.values[i]
		}
	}
	return false
}

func BestHand(hands []string) ([]string, error) {
	if len(hands) == 0 {
		return nil, errors.New("no hands provided")
	}

	var parsed []Hand
	for _, hand := range hands {
		h, err := parseHand(hand)
		if err != nil {
			return nil, err
		}
		parsed = append(parsed, h)
	}

	sort.Slice(parsed, func(i, j int) bool {
		return compareHands(parsed[i], parsed[j])
	})

	best := []string{parsed[0].original}
	for i := 1; i < len(parsed); i++ {
		if parsed[i].strength == parsed[0].strength {
			if compareValues(parsed[i].values, parsed[0].values) {
				best = append(best, parsed[i].original)
			}
		} else {
			break
		}
	}

	return best, nil
}

func compareValues(a, b []int) bool {
	for i := 0; i < len(a); i++ {
		if a[i] != b[i] {
			return a[i] == b[i]
		}
	}
	return true
}
