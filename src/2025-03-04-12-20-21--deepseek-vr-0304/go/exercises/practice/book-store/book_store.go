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

	sort.Sort(sort.Reverse(sort.IntSlice(counts)))

	groups := make([]int, 6)
	for {
		k := 0
		for i := 0; i < 5 && counts[i] > 0; i++ {
			k++
		}
		if k == 0 {
			break
		}
		groups[k]++

		for i := 0; i < k; i++ {
			counts[i]--
		}
		sort.Sort(sort.Reverse(sort.IntSlice(counts)))
	}

	cost := 0
	for size, num := range groups {
		switch size {
		case 5:
			cost += num * 5 * 600
		case 4:
			cost += num * 4 * 640
		case 3:
			cost += num * 3 * 720
		case 2:
			cost += num * 2 * 760
		case 1:
			cost += num * 800
		}
	}

	replacements := groups[5]
	if groups[3] < replacements {
		replacements = groups[3]
	}
	cost -= replacements * 40

	return cost
}
