package twobucket

import (
	"errors"
)

type state struct {
	bucket1 int
	bucket2 int
	steps   int
}

func Solve(sizeBucketOne, sizeBucketTwo, goalAmount int, startBucket string) (string, int, int, error) {
	// Validate inputs
	if sizeBucketOne <= 0 || sizeBucketTwo <= 0 || goalAmount <= 0 {
		return "", 0, 0, errors.New("invalid bucket size or goal amount")
	}
	if startBucket != "one" && startBucket != "two" {
		return "", 0, 0, errors.New("invalid start bucket")
	}

	// Initial state
	initial := state{0, 0, 0}
	if startBucket == "one" {
		initial.bucket1 = sizeBucketOne
	} else {
		initial.bucket2 = sizeBucketTwo
	}
	initial.steps++

	// BFS queue
	queue := []state{initial}
	visited := make(map[[2]int]bool)

	for len(queue) > 0 {
		current := queue[0]
		queue = queue[1:]

		// Check if we've reached the goal
		if current.bucket1 == goalAmount {
			return "one", current.steps, current.bucket2, nil
		}
		if current.bucket2 == goalAmount {
			return "two", current.steps, current.bucket1, nil
		}

		// Mark current state as visited
		key := [2]int{current.bucket1, current.bucket2}
		if visited[key] {
			continue
		}
		visited[key] = true

		// Generate possible next states
		nextStates := []state{
			// Fill bucket1
			{sizeBucketOne, current.bucket2, current.steps + 1},
			// Fill bucket2
			{current.bucket1, sizeBucketTwo, current.steps + 1},
			// Empty bucket1
			{0, current.bucket2, current.steps + 1},
			// Empty bucket2
			{current.bucket1, 0, current.steps + 1},
			// Pour bucket1 to bucket2
			{max(0, current.bucket1-(sizeBucketTwo-current.bucket2)),
				min(sizeBucketTwo, current.bucket2+current.bucket1),
				current.steps + 1},
			// Pour bucket2 to bucket1
			{min(sizeBucketOne, current.bucket1+current.bucket2),
				max(0, current.bucket2-(sizeBucketOne-current.bucket1)),
				current.steps + 1},
		}

		// Add valid next states to queue
		for _, next := range nextStates {
			if next.bucket1 >= 0 && next.bucket1 <= sizeBucketOne &&
				next.bucket2 >= 0 && next.bucket2 <= sizeBucketTwo {
				// Check the rule: starting bucket can't be empty while other is full
				if startBucket == "one" {
					if next.bucket1 == 0 && next.bucket2 == sizeBucketTwo {
						continue
					}
				} else {
					if next.bucket2 == 0 && next.bucket1 == sizeBucketOne {
						continue
					}
				}

				// Add to queue if not visited
				key := [2]int{next.bucket1, next.bucket2}
				if !visited[key] {
					queue = append(queue, next)
				}
			}
		}
	}

	return "", 0, 0, errors.New("no solution found")
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}
