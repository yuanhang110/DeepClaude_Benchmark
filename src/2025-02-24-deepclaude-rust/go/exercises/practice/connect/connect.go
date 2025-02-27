package connect

import (
	"strings"
)

func ResultOf(lines []string) (string, error) {
	if len(lines) == 0 {
		return "", nil
	}

	// Process grid by removing all spaces
	grid := make([][]rune, len(lines))
	for i, line := range lines {
		grid[i] = []rune(strings.ReplaceAll(line, " ", ""))
	}

	// Check O's top-to-bottom connection
	oVisited := make([][]bool, len(grid))
	for i := range oVisited {
		oVisited[i] = make([]bool, len(grid[i]))
	}

	var queue [][2]int
	directions := [][2]int{{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, -1}}

	// Initialize O's BFS from top row
	for c := 0; c < len(grid[0]); c++ {
		if grid[0][c] == 'O' {
			queue = append(queue, [2]int{0, c})
			oVisited[0][c] = true
		}
	}

	foundO := false
	for len(queue) > 0 {
		cell := queue[0]
		queue = queue[1:]
		r, c := cell[0], cell[1]

		if r == len(grid)-1 {
			foundO = true
			break
		}

		for _, dir := range directions {
			nr, nc := r+dir[0], c+dir[1]
			if nr >= 0 && nr < len(grid) && nc >= 0 && nc < len(grid[nr]) {
				if grid[nr][nc] == 'O' && !oVisited[nr][nc] {
					oVisited[nr][nc] = true
					queue = append(queue, [2]int{nr, nc})
				}
			}
		}
	}

	if foundO {
		return "O", nil
	}

	// Check X's left-to-right connection
	xVisited := make([][]bool, len(grid))
	for i := range xVisited {
		xVisited[i] = make([]bool, len(grid[i]))
	}

	queue = queue[:0] // Reset queue

	// Initialize X's BFS from leftmost column
	for r := 0; r < len(grid); r++ {
		if len(grid[r]) > 0 && grid[r][0] == 'X' {
			queue = append(queue, [2]int{r, 0})
			xVisited[r][0] = true
		}
	}

	foundX := false
	for len(queue) > 0 {
		cell := queue[0]
		queue = queue[1:]
		r, c := cell[0], cell[1]

		if c == len(grid[r])-1 {
			foundX = true
			break
		}

		for _, dir := range directions {
			nr, nc := r+dir[0], c+dir[1]
			if nr >= 0 && nr < len(grid) && nc >= 0 && nc < len(grid[nr]) {
				if grid[nr][nc] == 'X' && !xVisited[nr][nc] {
					xVisited[nr][nc] = true
					queue = append(queue, [2]int{nr, nc})
				}
			}
		}
	}

	if foundX {
		return "X", nil
	}

	return "", nil
}
