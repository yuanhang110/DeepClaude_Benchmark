package poker

import (
	"errors"
	"fmt"
	"sort"
	"strings"
)

// Card represents a playing card with rank and suit
type Card struct {
	Rank int
	Suit rune
}

// Hand represents a poker hand with its evaluation data
type Hand struct {
	Original string
	Cards    []Card
	Rank     int    // 8=straight flush, 7=four of a kind, 6=full house, etc.
	Values   []int  // Values for tie-breaking in descending priority
}

func BestHand(hands []string) ([]string, error) {
	if len(hands) == 0 {
		return nil, errors.New("no hands provided")
	}

	var evaluatedHands []Hand
	for _, h := range hands {
		cards, err := parseHand(h)
		if err != nil {
			return nil, err
		}
		evaluated := evaluateHand(cards, h)
		evaluatedHands = append(evaluatedHands, evaluated)
	}

	// Sort hands by rank (highest first)
	sort.Slice(evaluatedHands, func(i, j int) bool {
		return compareHands(evaluatedHands[i], evaluatedHands[j]) > 0
	})

	// Find all hands that tie for best
	bestHands := []string{evaluatedHands[0].Original}
	for i := 1; i < len(evaluatedHands); i++ {
		if compareHands(evaluatedHands[i], evaluatedHands[0]) == 0 {
			bestHands = append(bestHands, evaluatedHands[i].Original)
		} else {
			break
		}
	}

	return bestHands, nil
}

// parseHand converts a hand string into a slice of Cards
func parseHand(handStr string) ([]Card, error) {
	cardStrs := strings.Fields(handStr)
	if len(cardStrs) != 5 {
		return nil, fmt.Errorf("invalid hand: must contain exactly 5 cards")
	}

	var cards []Card
	for _, cardStr := range cardStrs {
		card, err := parseCard(cardStr)
		if err != nil {
			return nil, err
		}
		cards = append(cards, card)
	}

	return cards, nil
}

// parseCard converts a card string into a Card struct
func parseCard(cardStr string) (Card, error) {
	if len(cardStr) < 2 {
		return Card{}, fmt.Errorf("invalid card: %s", cardStr)
	}

	var rankStr string
	var suitRune rune
	
	// Handle 10 as a special case since it's two characters
	if len(cardStr) >= 3 && cardStr[:2] == "10" {
		if len(cardStr) != 3 {
			return Card{}, fmt.Errorf("invalid card: %s", cardStr)
		}
		rankStr = "10"
		suitRune = rune(cardStr[2])
	} else {
		if len(cardStr) != 2 {
			return Card{}, fmt.Errorf("invalid card: %s", cardStr)
		}
		rankStr = cardStr[:1]
		suitRune = rune(cardStr[1])
	}
	
	// Validate the suit
	if suitRune != '♤' && suitRune != '♡' && suitRune != '♢' && suitRune != '♧' {
		return Card{}, fmt.Errorf("invalid suit: %c", suitRune)
	}
	
	// Convert rank to integer
	var rank int
	switch rankStr {
	case "A":
		rank = 14
	case "K":
		rank = 13
	case "Q":
		rank = 12
	case "J":
		rank = 11
	case "10":
		rank = 10
	case "9":
		rank = 9
	case "8":
		rank = 8
	case "7":
		rank = 7
	case "6":
		rank = 6
	case "5":
		rank = 5
	case "4":
		rank = 4
	case "3":
		rank = 3
	case "2":
		rank = 2
	default:
		return Card{}, fmt.Errorf("invalid rank: %s", rankStr)
	}

	return Card{Rank: rank, Suit: suitRune}, nil
}

// evaluateHand determines the hand's rank and tie-breaker values
func evaluateHand(cards []Card, original string) Hand {
	hand := Hand{
		Original: original,
		Cards:    cards,
	}

	// Get sorted ranks
	ranks := make([]int, len(cards))
	for i, card := range cards {
		ranks[i] = card.Rank
	}
	sort.Slice(ranks, func(i, j int) bool {
		return ranks[i] > ranks[j]
	})
	
	// Count frequencies of each rank and suit
	rankCount := make(map[int]int)
	suitCount := make(map[rune]int)
	
	for _, card := range cards {
		rankCount[card.Rank]++
		suitCount[card.Suit]++
	}
	
	// Check for flush (all same suit)
	isFlush := len(suitCount) == 1
	
	// Check for straight
	isStraight := false
	sortedRanks := make([]int, len(ranks))
	copy(sortedRanks, ranks)
	sort.Ints(sortedRanks)
	
	// Regular straight
	if sortedRanks[4]-sortedRanks[0] == 4 && len(rankCount) == 5 {
		isStraight = true
	}
	
	// A-5-4-3-2 straight
	if sortedRanks[0] == 2 && sortedRanks[1] == 3 && sortedRanks[2] == 4 && 
	   sortedRanks[3] == 5 && sortedRanks[4] == 14 {
		isStraight = true
		// Ace is low in this case, update the ranks array for correct value comparison
		for i, r := range ranks {
			if r == 14 {
				ranks[i] = 1 // Ace is now low
				break
			}
		}
		// Re-sort after changing Ace value
		sort.Slice(ranks, func(i, j int) bool {
			return ranks[i] > ranks[j]
		})
	}
	
	// Group cards by count
	var quads, trips, pairs []int
	var singles []int
	
	for rank, count := range rankCount {
		switch count {
		case 4:
			quads = append(quads, rank)
		case 3:
			trips = append(trips, rank)
		case 2:
			pairs = append(pairs, rank)
		case 1:
			singles = append(singles, rank)
		}
	}
	
	// Sort groups by rank
	sort.Sort(sort.Reverse(sort.IntSlice(quads)))
	sort.Sort(sort.Reverse(sort.IntSlice(trips)))
	sort.Sort(sort.Reverse(sort.IntSlice(pairs)))
	sort.Sort(sort.Reverse(sort.IntSlice(singles)))
	
	// Determine hand rank and values for tie-breaking
	if isFlush && isStraight {
		hand.Rank = 8 // Straight flush
		hand.Values = []int{ranks[0]} // Highest card in straight
	} else if len(quads) > 0 {
		hand.Rank = 7 // Four of a kind
		hand.Values = append(quads, singles...)
	} else if len(trips) > 0 && len(pairs) > 0 {
		hand.Rank = 6 // Full house
		hand.Values = append(trips, pairs...)
	} else if isFlush {
		hand.Rank = 5 // Flush
		hand.Values = ranks
	} else if isStraight {
		hand.Rank = 4 // Straight
		hand.Values = []int{ranks[0]} // Highest card in straight
	} else if len(trips) > 0 {
		hand.Rank = 3 // Three of a kind
		hand.Values = append(trips, singles...)
	} else if len(pairs) >= 2 {
		hand.Rank = 2 // Two pair
		hand.Values = append(pairs, singles...)
	} else if len(pairs) == 1 {
		hand.Rank = 1 // One pair
		hand.Values = append(pairs, singles...)
	} else {
		hand.Rank = 0 // High card
		hand.Values = ranks
	}
	
	return hand
}

// compareHands compares two hands, returns:
// 1 if a > b, 0 if a == b, -1 if a < b
func compareHands(a, b Hand) int {
	// Compare ranks
	if a.Rank > b.Rank {
		return 1
	}
	if a.Rank < b.Rank {
		return -1
	}
	
	// Same rank, compare values for tie-breaking
	for i := 0; i < len(a.Values) && i < len(b.Values); i++ {
		if a.Values[i] > b.Values[i] {
			return 1
		}
		if a.Values[i] < b.Values[i] {
			return -1
		}
	}
	
	// If we're here and values arrays have different lengths, the longer one wins
	if len(a.Values) > len(b.Values) {
		return 1
	}
	if len(a.Values) < len(b.Values) {
		return -1
	}
	
	// Hands are identical
	return 0
}
