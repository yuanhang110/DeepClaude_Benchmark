package poker

import (
	"fmt"
	"sort"
	"strconv"
	"strings"
)

type Card struct {
	Rank int
	Suit rune
}

type HandRank struct {
	Type   int
	Values []int
}

const (
	HighCard = iota
	OnePair
	TwoPairs
	ThreeOfAKind
	Straight
	Flush
	FullHouse
	FourOfAKind
	StraightFlush
)

func parseHand(hand string) ([]Card, error) {
	cards := strings.Split(hand, " ")
	if len(cards) != 5 {
		return nil, fmt.Errorf("invalid hand: %s", hand)
	}

	var result []Card
	for _, cardStr := range cards {
		card, err := parseCard(cardStr)
		if err != nil {
			return nil, err
		}
		result = append(result, card)
	}
	return result, nil
}

func parseCard(cardStr string) (Card, error) {
	runes := []rune(cardStr)
	
	// Handle 10 case with proper rune handling
	if len(runes) == 3 && runes[0] == '1' && runes[1] == '0' {
		return Card{Rank: 10, Suit: runes[2]}, nil
	}
	
	if len(runes) != 2 {
		return Card{}, fmt.Errorf("invalid card: %s", cardStr)
	}

	rankChar := runes[0]
	suit := runes[1]

	var rank int
	switch rankChar {
	case 'J':
		rank = 11
	case 'Q':
		rank = 12
	case 'K':
		rank = 13
	case 'A':
		rank = 14
	default:
		var err error
		rank, err = strconv.Atoi(string(rankChar))
		if err != nil || rank < 2 || rank > 9 {
			return Card{}, fmt.Errorf("invalid rank: %c", rankChar)
		}
	}

	return Card{Rank: rank, Suit: suit}, nil
}

func evaluateHand(hand []Card) HandRank {
	sorted := make([]Card, len(hand))
	copy(sorted, hand)
	sort.Slice(sorted, func(i, j int) bool { return sorted[i].Rank < sorted[j].Rank })

	isFlush := true
	suit := sorted[0].Suit
	for _, c := range sorted[1:] {
		if c.Suit != suit {
			isFlush = false
			break
		}
	}

	isStraight := true
	for i := 1; i < len(sorted); i++ {
		if sorted[i].Rank != sorted[i-1].Rank+1 {
			if i == len(sorted)-1 && sorted[0].Rank == 2 && sorted[1].Rank == 3 &&
				sorted[2].Rank == 4 && sorted[3].Rank == 5 && sorted[4].Rank == 14 {
				// Ace-low straight (5-high)
				return HandRank{
					Type:   Straight,
					Values: []int{5},
				}
			}
			isStraight = false
			break
		}
	}

	// Ace-low straight check
	if !isStraight && sorted[0].Rank == 2 && sorted[1].Rank == 3 && sorted[2].Rank == 4 &&
		sorted[3].Rank == 5 && sorted[4].Rank == 14 {
		isStraight = true
		sorted = []Card{sorted[0], sorted[1], sorted[2], sorted[3], {Rank: 1, Suit: sorted[4].Suit}}
		sort.Slice(sorted, func(i, j int) bool { return sorted[i].Rank < sorted[j].Rank })
	}

	rankCount := make(map[int]int)
	for _, c := range sorted {
		rankCount[c.Rank]++
	}

	var pairs, trips, quad []int
	var values []int
	for rank, count := range rankCount {
		switch count {
		case 2:
			pairs = append(pairs, rank)
		case 3:
			trips = append(trips, rank)
		case 4:
			quad = append(quad, rank)
		}
	}
	sort.Sort(sort.Reverse(sort.IntSlice(pairs)))
	sort.Sort(sort.Reverse(sort.IntSlice(trips)))

	// Determine hand type
	switch {
	case isStraight && isFlush:
		return HandRank{
			Type:   StraightFlush,
			Values: []int{sorted[len(sorted)-1].Rank},
		}

	case len(quad) == 1:
		kicker := 0
		for _, c := range sorted {
			if c.Rank != quad[0] {
				kicker = c.Rank
			}
		}
		return HandRank{
			Type:   FourOfAKind,
			Values: []int{quad[0], kicker},
		}

	case len(trips) == 1 && len(pairs) == 1:
		return HandRank{
			Type:   FullHouse,
			Values: []int{trips[0], pairs[0]},
		}

	case isFlush:
		values = make([]int, 5)
		for i := range sorted {
			values[i] = sorted[len(sorted)-1-i].Rank
		}
		return HandRank{
			Type:   Flush,
			Values: values,
		}

	case isStraight:
		return HandRank{
			Type:   Straight,
			Values: []int{sorted[len(sorted)-1].Rank},
		}

	case len(trips) == 1:
		kickers := make([]int, 0)
		for _, c := range sorted {
			if c.Rank != trips[0] {
				kickers = append(kickers, c.Rank)
			}
		}
		sort.Sort(sort.Reverse(sort.IntSlice(kickers)))
		return HandRank{
			Type:   ThreeOfAKind,
			Values: append([]int{trips[0]}, kickers...),
		}

	case len(pairs) >= 2:
		kicker := 0
		for _, c := range sorted {
			if c.Rank != pairs[0] && c.Rank != pairs[1] {
				kicker = c.Rank
				break
			}
		}
		return HandRank{
			Type:   TwoPairs,
			Values: []int{pairs[0], pairs[1], kicker},
		}

	case len(pairs) == 1:
		kickers := make([]int, 0)
		for _, c := range sorted {
			if c.Rank != pairs[0] {
				kickers = append(kickers, c.Rank)
			}
		}
		sort.Sort(sort.Reverse(sort.IntSlice(kickers)))
		return HandRank{
			Type:   OnePair,
			Values: append([]int{pairs[0]}, kickers...),
		}

	default:
		values = make([]int, 5)
		for i := range sorted {
			values[i] = sorted[len(sorted)-1-i].Rank
		}
		return HandRank{
			Type:   HighCard,
			Values: values,
		}
	}
}

func compareHandRanks(a, b HandRank) int {
	if a.Type != b.Type {
		return a.Type - b.Type
	}
	for i := range a.Values {
		if a.Values[i] != b.Values[i] {
			return a.Values[i] - b.Values[i]
		}
	}
	return 0
}

func BestHand(hands []string) ([]string, error) {
	var maxRank HandRank
	var bestHands []string

	for _, handStr := range hands {
		hand, err := parseHand(handStr)
		if err != nil {
			return nil, err
		}

		currentRank := evaluateHand(hand)

		if len(bestHands) == 0 {
			maxRank = currentRank
			bestHands = []string{handStr}
			continue
		}

		comparison := compareHandRanks(currentRank, maxRank)
		if comparison > 0 {
			maxRank = currentRank
			bestHands = []string{handStr}
		} else if comparison == 0 {
			bestHands = append(bestHands, handStr)
		}
	}

	return bestHands, nil
}
