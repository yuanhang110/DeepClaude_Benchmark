package poker

import (
	"fmt"
	"sort"
	"strings"
)

type Card struct {
	Rank  string
	Suit  string
	Value int
}

func BestHand(hands []string) ([]string, error) {
	if len(hands) == 0 {
		return nil, nil
	}

	var bestHands []string
	var maxScore []int

	for _, handStr := range hands {
		score, err := evaluateHand(handStr)
		if err != nil {
			return nil, err
		}

		if len(bestHands) == 0 {
			bestHands = []string{handStr}
			maxScore = score
			continue
		}

		cmp := compareScores(score, maxScore)
		if cmp > 0 {
			bestHands = []string{handStr}
			maxScore = score
		} else if cmp == 0 {
			bestHands = append(bestHands, handStr)
		}
	}

	return bestHands, nil
}

func evaluateHand(handStr string) ([]int, error) {
	cards, err := parseHand(handStr)
	if err != nil {
		return nil, err
	}

	values := make([]int, len(cards))
	suits := make([]string, len(cards))
	for i, card := range cards {
		values[i] = card.Value
		suits[i] = card.Suit
	}

	flush := isFlush(suits)
	straight, high := isStraight(values)

	if flush && straight {
		return []int{8, high}, nil
	}

	counts := countRanks(values)
	pairs := make([]int, 0)
	three := 0
	four := 0

	for rank, count := range counts {
		switch count {
		case 2:
			pairs = append(pairs, rank)
		case 3:
			three = rank
		case 4:
			four = rank
		}
	}

	if four > 0 {
		kickers := make([]int, 0)
		for _, v := range values {
			if v != four {
				kickers = append(kickers, v)
			}
		}
		return []int{7, four, kickers[0]}, nil
	}

	if three > 0 && len(pairs) >= 1 {
		return []int{6, three, pairs[0]}, nil
	}

	if flush {
		sorted := make([]int, len(values))
		copy(sorted, values)
		sort.Sort(sort.Reverse(sort.IntSlice(sorted)))
		return append([]int{5}, sorted...), nil
	}

	if straight {
		return []int{4, high}, nil
	}

	if three > 0 {
		kickers := make([]int, 0)
		for _, v := range values {
			if v != three {
				kickers = append(kickers, v)
			}
		}
		sort.Sort(sort.Reverse(sort.IntSlice(kickers)))
		return append([]int{3, three}, kickers...), nil
	}

	if len(pairs) >= 2 {
		sort.Sort(sort.Reverse(sort.IntSlice(pairs)))
		kicker := 0
		for _, v := range values {
			if v != pairs[0] && v != pairs[1] {
				kicker = v
				break
			}
		}
		return []int{2, pairs[0], pairs[1], kicker}, nil
	}

	if len(pairs) == 1 {
		kickers := make([]int, 0)
		for _, v := range values {
			if v != pairs[0] {
				kickers = append(kickers, v)
			}
		}
		sort.Sort(sort.Reverse(sort.IntSlice(kickers)))
		return append([]int{1, pairs[0]}, kickers...), nil
	}

	sorted := make([]int, len(values))
	copy(sorted, values)
	sort.Sort(sort.Reverse(sort.IntSlice(sorted)))
	return append([]int{0}, sorted...), nil
}

func parseHand(handStr string) ([]Card, error) {
	cardStrs := strings.Split(handStr, " ")
	if len(cardStrs) != 5 {
		return nil, fmt.Errorf("invalid hand: %s", handStr)
	}

	cards := make([]Card, 0, 5)
	for _, cs := range cardStrs {
		card, err := parseCard(cs)
		if err != nil {
			return nil, err
		}
		cards = append(cards, card)
	}

	sort.Slice(cards, func(i, j int) bool {
		return cards[i].Value < cards[j].Value
	})

	return cards, nil
}

func parseCard(cardStr string) (Card, error) {
	if len(cardStr) < 2 {
		return Card{}, fmt.Errorf("invalid card: %s", cardStr)
	}

	runes := []rune(cardStr)
	suitRune := runes[len(runes)-1]
	suit := string(suitRune)
	rankRunes := runes[:len(runes)-1]
	rankStr := string(rankRunes)

	value, ok := rankValues[rankStr]
	if !ok {
		return Card{}, fmt.Errorf("invalid rank: %s", rankStr)
	}

	return Card{
		Rank:  rankStr,
		Suit:  suit,
		Value: value,
	}, nil
}

var rankValues = map[string]int{
	"2":  2,
	"3":  3,
	"4":  4,
	"5":  5,
	"6":  6,
	"7":  7,
	"8":  8,
	"9":  9,
	"10": 10,
	"J":  11,
	"Q":  12,
	"K":  13,
	"A":  14,
}

func isFlush(suits []string) bool {
	first := suits[0]
	for _, s := range suits[1:] {
		if s != first {
			return false
		}
	}
	return true
}

func isStraight(values []int) (bool, int) {
	sorted := make([]int, len(values))
	copy(sorted, values)
	sort.Ints(sorted)

	// Check normal straight
	isConsecutive := true
	for i := 1; i < len(sorted); i++ {
		if sorted[i] != sorted[i-1]+1 {
			isConsecutive = false
			break
		}
	}

	if isConsecutive {
		return true, sorted[len(sorted)-1]
	}

	// Check Ace-low straight (A,2,3,4,5)
	if sorted[0] == 2 && sorted[1] == 3 && sorted[2] == 4 && sorted[3] == 5 && sorted[4] == 14 {
		return true, 5
	}

	return false, 0
}

func countRanks(values []int) map[int]int {
	counts := make(map[int]int)
	for _, v := range values {
		counts[v]++
	}
	return counts
}

func compareScores(a, b []int) int {
	for i := 0; i < len(a) && i < len(b); i++ {
		if a[i] > b[i] {
			return 1
		} else if a[i] < b[i] {
			return -1
		}
	}
	if len(a) > len(b) {
		return 1
	} else if len(a) < len(b) {
		return -1
	}
	return 0
}
