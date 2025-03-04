package connect

import (
	"errors"
)

type cell struct {
	x, y int
}

func ResultOf(lines []string) (string, error) {
	if len(lines) == 0 {
		return "", errors.New("empty board")
	}

	// Check for O's vertical connection (top to bottom)
	visited := make(map[cell]bool)
	for x := 0; x < len(lines[0]); x++ {
		if lines[0][x] == 'O' {
			if dfs(cell{x, 0}, 'O', lines, visited, func(c cell) bool {
				return c.y == len(lines)-1
			}) {
				return "O", nil
			}
		}
	}

	// Check for X's horizontal connection (left to right)
	visited = make(map[cell]bool)
	for y := 0; y < len(lines); y++ {
		if lines[y][0] == 'X' {
			if dfs(cell{0, y}, 'X', lines, visited, func(c cell) bool {
				return c.x == len(lines[y])-1
			}) {
				return "X", nil
			}
		}
	}

	return "", nil
}

func dfs(c cell, player byte, lines []string, visited map[cell]bool, isGoal func(cell) bool) bool {
	// Check if we're out of bounds or already visited
	if c.y < 0 || c.y >= len(lines) || c.x < 0 || c.x >= len(lines[c.y]) || visited[c] {
		return false
	}

	// Check if this cell belongs to the player
	if lines[c.y][c.x] != player {
		return false
	}

	// Check if we've reached the goal
	if isGoal(c) {
		return true
	}

	visited[c] = true

	// Explore all 6 possible directions in a hex grid
	directions := []cell{
		{1, 0}, {1, -1}, {0, -1},
		{-1, 0}, {-1, 1}, {0, 1},
	}

	for _, dir := range directions {
		next := cell{c.x + dir.x, c.y + dir.y}
		if dfs(next, player, lines, visited, isGoal) {
			return true
		}
	}

	return false
}
