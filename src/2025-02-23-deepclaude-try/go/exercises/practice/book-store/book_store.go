package bookstore

import (
	"sort"
)

func Cost(books []int) int {
	counts := make([]int, 5)
	for _, b := range books {
		counts[b-1]++
	}

	groups := make([]int, 6) // indexes 0-5 (0 unused)

	// Form groups greedily
	for {
		// Sort counts in descending order
		sort.Slice(counts, func(i, j int) bool {
			return counts[i] > counts[j]
		})

		// Find maximum possible group size
		maxGroup := 0
		for maxGroup < 5 && counts[maxGroup] > 0 {
			maxGroup++
		}

		if maxGroup == 0 {
			break
		}

		groups[maxGroup]++
		for i := 0; i < maxGroup; i++ {
			counts[i]--
		}
	}

	// Adjust groups: replace 5+3 with 4+4
	replacements := min(groups[5], groups[3])
	groups[5] -= replacements
	groups[3] -= replacements
	groups[4] += replacements * 2

	// Calculate total cost
	total := 0
	for k, cnt := range groups {
		switch k {
		case 1:
			total += cnt * 800
		case 2:
			total += cnt * (2 * 800 * 95 / 100)
		case 3:
			total += cnt * (3 * 800 * 90 / 100)
		case 4:
			total += cnt * (4 * 800 * 80 / 100)
		case 5:
			total += cnt * (5 * 800 * 75 / 100)
		}
	}

	return total
}

// Helper function to avoid using math.Min which uses float64
func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
