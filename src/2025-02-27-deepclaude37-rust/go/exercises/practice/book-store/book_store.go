package bookstore

import (
	"sort"
)

func Cost(books []int) int {
	counts := make([]int, 5)
	for _, b := range books {
		counts[b-1]++
	}

	groups := make([]int, 6)

	for {
		sort.Sort(sort.Reverse(sort.IntSlice(counts)))
		max := 0
		for i := 0; i < 5; i++ {
			if counts[i] > 0 {
				max = i + 1
			} else {
				break
			}
		}
		if max == 0 {
			break
		}
		groups[max]++
		for i := 0; i < max; i++ {
			counts[i]--
		}
	}

	replacements := min(groups[5], groups[3])
	groups[5] -= replacements
	groups[3] -= replacements
	groups[4] += 2 * replacements

	total := 0
	for s := 1; s <= 5; s++ {
		total += groups[s] * calculateGroupCost(s)
	}
	return total
}

func calculateGroupCost(size int) int {
	switch size {
	case 1:
		return 800
	case 2:
		return int(800 * 2 * 0.95)
	case 3:
		return int(800 * 3 * 0.90)
	case 4:
		return int(800 * 4 * 0.80)
	case 5:
		return int(800 * 5 * 0.75)
	default:
		return 0
	}
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
