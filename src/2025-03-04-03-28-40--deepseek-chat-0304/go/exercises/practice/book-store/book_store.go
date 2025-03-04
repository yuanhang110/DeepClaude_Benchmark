package bookstore

func Cost(books []int) int {
	// Count how many of each book we have
	counts := make(map[int]int)
	for _, book := range books {
		counts[book]++
	}

	// Convert counts to a slice and sort descending
	var groups []int
	for _, count := range counts {
		groups = append(groups, count)
	}

	// Create a slice to store all groups we form
	var groupSizes []int

	// First pass: create as many large groups as possible
	for {
		groupSize := 0
		for i := range groups {
			if groups[i] > 0 {
				groupSize++
				groups[i]--
			}
		}
		if groupSize == 0 {
			break
		}
		groupSizes = append(groupSizes, groupSize)
	}

	// Second pass: optimize groups by replacing 5+3 with 4+4 where possible
	for {
		// Count how many groups of 5 and 3 we have
		count5 := 0
		count3 := 0
		for i, size := range groupSizes {
			if size == 5 {
				count5++
			}
			if size == 3 {
				count3++
			}
		}

		// If we have at least one group of 5 and one group of 3, replace them with two groups of 4
		if count5 > 0 && count3 > 0 {
			// Remove one group of 5 and one group of 3
			for i := 0; i < len(groupSizes); i++ {
				if groupSizes[i] == 5 {
					groupSizes[i] = 4
					break
				}
			}
			for i := 0; i < len(groupSizes); i++ {
				if groupSizes[i] == 3 {
					groupSizes[i] = 4
					break
				}
			}
		} else {
			break
		}
	}

	// Calculate total cost based on optimized groups
	total := 0
	for _, size := range groupSizes {
		switch size {
		case 1:
			total += 800
		case 2:
			total += 1520
		case 3:
			total += 2160
		case 4:
			total += 2560
		case 5:
			total += 3000
		}
	}

	return total
}
