package twobucket

import (
	"errors"
	"fmt"
)

type state struct {
	a, b  int
	steps int
}

func Solve(sizeBucketOne, sizeBucketTwo, goalAmount int, startBucket string) (string, int, int, error) {
	// Validate input parameters
	if sizeBucketOne <= 0 || sizeBucketTwo <= 0 || goalAmount <= 0 {
		return "", 0, 0, errors.New("invalid bucket sizes or goal")
	}
	if startBucket != "one" && startBucket != "two" {
		return "", 0, 0, errors.New("invalid start bucket")
	}
	if goalAmount > sizeBucketOne && goalAmount > sizeBucketTwo {
		return "", 0, 0, errors.New("goal exceeds both bucket capacities")
	}

	// Check GCD condition for impossibility
	if gcd(sizeBucketOne, sizeBucketTwo) != 0 && goalAmount%gcd(sizeBucketOne, sizeBucketTwo) != 0 {
		return "", 0, 0, errors.New("no solution exists")
	}

	// Check if initial fill matches goal
	if startBucket == "one" && sizeBucketOne == goalAmount {
		return "one", 1, 0, nil
	}
	if startBucket == "two" && sizeBucketTwo == goalAmount {
		return "two", 1, 0, nil
	}

	// Initialize BFS
	queue := []state{}
	visited := make(map[string]bool)
	if startBucket == "one" {
		queue = append(queue, state{sizeBucketOne, 0, 1})
		visited[fmt.Sprintf("%d,%d", sizeBucketOne, 0)] = true
	} else {
		queue = append(queue, state{0, sizeBucketTwo, 1})
		visited[fmt.Sprintf("%d,%d", 0, sizeBucketTwo)] = true
	}

	for len(queue) > 0 {
		current := queue[0]
		queue = queue[1:]

		// Check if we've reached the goal
		if current.a == goalAmount {
			return "one", current.steps, current.b, nil
		}
		if current.b == goalAmount {
			return "two", current.steps, current.a, nil
		}

		// Generate all possible next states
		nextStates := []state{
			// Fill bucket 1
			{sizeBucketOne, current.b, current.steps + 1},
			// Fill bucket 2
			{current.a, sizeBucketTwo, current.steps + 1},
			// Empty bucket 1
			{0, current.b, current.steps + 1},
			// Empty bucket 2
			{current.a, 0, current.steps + 1},
			// Pour from 1 to 2
			{
				current.a - min(current.a, sizeBucketTwo-current.b),
				current.b + min(current.a, sizeBucketTwo-current.b),
				current.steps + 1,
			},
			// Pour from 2 to 1
			{
				current.a + min(current.b, sizeBucketOne-current.a),
				current.b - min(current.b, sizeBucketOne-current.a),
				current.steps + 1,
			},
		}

		for _, ns := range nextStates {
			// Validate state according to the rules
			if startBucket == "one" {
				if ns.a == 0 && ns.b == sizeBucketTwo {
					continue // Invalid state
				}
			} else {
				if ns.b == 0 && ns.a == sizeBucketOne {
					continue // Invalid state
				}
			}

			// Check if already visited
			key := fmt.Sprintf("%d,%d", ns.a, ns.b)
			if !visited[key] {
				visited[key] = true
				queue = append(queue, ns)
			}
		}
	}

	return "", 0, 0, errors.New("no solution possible")
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
