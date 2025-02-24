package bookstore

import (
	"sort"
)

func Cost(books []int) int {
	counts := make([]int, 5)
	for _, b := range books {
		if b < 1 || b > 5 {
			continue
		}
		counts[b-1]++
	}

	sort.Slice(counts, func(i, j int) bool { return counts[i] > counts[j] })

	var groups []int

	for {
		size := 0
		for i := 0; i < len(counts) && counts[i] > 0; i++ {
			size++
		}
		if size == 0 {
			break
		}
		groups = append(groups, size)
		for i := 0; i < size; i++ {
			counts[i]--
		}
		sort.Slice(counts, func(i, j int) bool { return counts[i] > counts[j] })
	}

	total := 0
	for _, g := range groups {
		switch g {
		case 1:
			total += 800
		case 2:
			total += int(2 * 800 * 0.95)
		case 3:
			total += int(3 * 800 * 0.90)
		case 4:
			total += int(4 * 800 * 0.80)
		case 5:
			total += int(5 * 800 * 0.75)
		}
	}

	fives := 0
	threes := 0
	for _, g := range groups {
		if g == 5 {
			fives++
		} else if g == 3 {
			threes++
		}
	}
	replacements := min(fives, threes)
	total -= replacements * 40

	return total
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
