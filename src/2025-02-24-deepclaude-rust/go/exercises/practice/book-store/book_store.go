package bookstore

import (
	"sort"
)

var groupCost = [6]int{0, 800, 1520, 2160, 2560, 3000}

func Cost(books []int) int {
	counts := make(map[int]int)
	for _, book := range books {
		counts[book]++
	}

	var list []int
	for _, c := range counts {
		list = append(list, c)
	}

	// Pad with zeros to ensure exactly 5 elements
	for len(list) < 5 {
		list = append(list, 0)
	}

	// Sort in descending order
	sort.Slice(list, func(i, j int) bool {
		return list[i] > list[j]
	})

	var state [5]int
	copy(state[:], list)

	memo := make(map[[5]int]int)
	return calculate(state, memo)
}

func calculate(state [5]int, memo map[[5]int]int) int {
	if val, ok := memo[state]; ok {
		return val
	}

	sum := 0
	for _, c := range state {
		sum += c
	}
	if sum == 0 {
		return 0
	}

	minCost := -1
	for k := 5; k >= 1; k-- {
		// Check if we can form a group of size k
		valid := true
		for i := 0; i < k; i++ {
			if state[i] == 0 {
				valid = false
				break
			}
		}

		if valid {
			newState := state
			// Subtract 1 from each of the first k elements
			for i := 0; i < k; i++ {
				newState[i]--
			}
			// Sort the new state
			sort.Slice(newState[:], func(i, j int) bool {
				return newState[i] > newState[j]
			})

			cost := groupCost[k] + calculate(newState, memo)
			if minCost == -1 || cost < minCost {
				minCost = cost
			}
		}
	}

	memo[state] = minCost
	return minCost
}
