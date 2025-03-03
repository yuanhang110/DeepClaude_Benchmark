package twobucket

import (
	"errors"
)

func Solve(sizeBucketOne, sizeBucketTwo, goalAmount int, startBucket string) (string, int, int, error) {
	// Validate parameters
	if sizeBucketOne <= 0 || sizeBucketTwo <= 0 {
		return "", 0, 0, errors.New("invalid bucket sizes")
	}
	if goalAmount <= 0 {
		return "", 0, 0, errors.New("invalid goal amount")
	}
	if startBucket != "one" && startBucket != "two" {
		return "", 0, 0, errors.New("invalid start bucket")
	}
	if goalAmount > sizeBucketOne && goalAmount > sizeBucketTwo {
		return "", 0, 0, errors.New("goal is larger than both buckets")
	}

	// Check initial state
	var initialB1, initialB2 int
	if startBucket == "one" {
		initialB1 = sizeBucketOne
		initialB2 = 0
		if initialB1 == goalAmount {
			return "one", 1, initialB2, nil
		}
	} else {
		initialB1 = 0
		initialB2 = sizeBucketTwo
		if initialB2 == goalAmount {
			return "two", 1, initialB1, nil
		}
	}

	// Proceed with BFS
	type state struct {
		b1, b2 int
	}

	initialSteps := 1
	initialState := state{initialB1, initialB2}

	queue := []struct {
		s     state
		steps int
	}{
		{initialState, initialSteps},
	}

	visited := make(map[state]bool)
	visited[initialState] = true

	for len(queue) > 0 {
		current := queue[0]
		queue = queue[1:]

		currentState := current.s
		currentSteps := current.steps

		// Check if current state is a solution
		if currentState.b1 == goalAmount {
			return "one", currentSteps, currentState.b2, nil
		}
		if currentState.b2 == goalAmount {
			return "two", currentSteps, currentState.b1, nil
		}

		// Generate all possible next states
		nextStates := []state{
			{sizeBucketOne, currentState.b2}, // Fill bucket one
			{currentState.b1, sizeBucketTwo}, // Fill bucket two
			{0, currentState.b2},             // Empty bucket one
			{currentState.b1, 0},             // Empty bucket two
			func() state { // Pour from one to two
				amount := min(currentState.b1, sizeBucketTwo-currentState.b2)
				return state{currentState.b1 - amount, currentState.b2 + amount}
			}(),
			func() state { // Pour from two to one
				amount := min(currentState.b2, sizeBucketOne-currentState.b1)
				return state{currentState.b1 + amount, currentState.b2 - amount}
			}(),
		}

		for _, ns := range nextStates {
			// Check if the new state is invalid (starting bucket empty and other full)
			if (startBucket == "one" && ns.b1 == 0 && ns.b2 == sizeBucketTwo) ||
				(startBucket == "two" && ns.b2 == 0 && ns.b1 == sizeBucketOne) {
				continue
			}

			// Skip invalid states or already visited states
			if ns.b1 < 0 || ns.b1 > sizeBucketOne || ns.b2 < 0 || ns.b2 > sizeBucketTwo || visited[ns] {
				continue
			}

			visited[ns] = true
			queue = append(queue, struct {
				s     state
				steps int
			}{ns, currentSteps + 1})
		}
	}

	// No solution found
	return "", 0, 0, errors.New("no solution possible")
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
