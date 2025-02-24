package twobucket

import "fmt"

func Solve(sizeBucketOne, sizeBucketTwo, goalAmount int, startBucket string) (string, int, int, error) {
	// Check for invalid parameters
	if sizeBucketOne <= 0 || sizeBucketTwo <= 0 || goalAmount <= 0 {
		return "", 0, 0, fmt.Errorf("invalid bucket sizes or goal amount")
	}
	if startBucket != "one" && startBucket != "two" {
		return "", 0, 0, fmt.Errorf("invalid start bucket")
	}
	if goalAmount > sizeBucketOne && goalAmount > sizeBucketTwo {
		return "", 0, 0, fmt.Errorf("goal is larger than both buckets")
	}

	// Check if GCD of bucket sizes doesn't divide goal (bonus requirement)
	d := gcd(sizeBucketOne, sizeBucketTwo)
	if goalAmount%d != 0 {
		return "", 0, 0, fmt.Errorf("no solution exists")
	}

	// Determine initial state based on start bucket
	var initialB1, initialB2 int
	if startBucket == "one" {
		initialB1 = sizeBucketOne
		initialB2 = 0
		if initialB1 == goalAmount {
			return "one", 1, 0, nil
		}
	} else {
		initialB1 = 0
		initialB2 = sizeBucketTwo
		if initialB2 == goalAmount {
			return "two", 1, 0, nil
		}
	}

	// Initialize BFS
	visited := make(map[[2]int]bool)
	queue := [][3]int{{initialB1, initialB2, 1}}
	visited[[2]int{initialB1, initialB2}] = true

	for len(queue) > 0 {
		current := queue[0]
		queue = queue[1:]

		// Generate all possible next states
		nextStates := make([][3]int, 0)

		// Fill bucket 1
		nextStates = append(nextStates, [3]int{sizeBucketOne, current[1], current[2] + 1})

		// Fill bucket 2
		nextStates = append(nextStates, [3]int{current[0], sizeBucketTwo, current[2] + 1})

		// Empty bucket 1
		nextStates = append(nextStates, [3]int{0, current[1], current[2] + 1})

		// Empty bucket 2
		nextStates = append(nextStates, [3]int{current[0], 0, current[2] + 1})

		// Pour from 1 to 2
		pour := min(current[0], sizeBucketTwo-current[1])
		nextStates = append(nextStates, [3]int{current[0] - pour, current[1] + pour, current[2] + 1})

		// Pour from 2 to 1
		pour = min(current[1], sizeBucketOne-current[0])
		nextStates = append(nextStates, [3]int{current[0] + pour, current[1] - pour, current[2] + 1})

		// Process each next state
		for _, ns := range nextStates {
			stateKey := [2]int{ns[0], ns[1]}
			if visited[stateKey] {
				continue
			}

			// Check if the state violates the post-action rule
			if startBucket == "one" {
				if ns[0] == 0 && ns[1] == sizeBucketTwo {
					continue
				}
			} else {
				if ns[1] == 0 && ns[0] == sizeBucketOne {
					continue
				}
			}

			// Check if goal is met in this state
			if ns[0] == goalAmount {
				return "one", ns[2], ns[1], nil
			}
			if ns[1] == goalAmount {
				return "two", ns[2], ns[0], nil
			}

			// Add to visited and queue
			visited[stateKey] = true
			queue = append(queue, ns)
		}
	}

	// If no solution found
	return "", 0, 0, fmt.Errorf("no solution exists")
}

func gcd(a, b int) int {
	for b != 0 {
		a, b = b, a%b
	}
	return a
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
