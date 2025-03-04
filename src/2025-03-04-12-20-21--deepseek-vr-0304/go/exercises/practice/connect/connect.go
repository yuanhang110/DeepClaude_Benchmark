package connect

import (
	"strings"
)

func ResultOf(lines []string) (string, error) {
	if len(lines) == 0 {
		return "", nil
	}

	// Create grid by trimming whitespace from each line
	grid := make([][]byte, len(lines))
	for i, line := range lines {
		trimmed := strings.TrimSpace(line)
		grid[i] = []byte(trimmed)
	}

	// Check O's path (top to bottom)
	if len(grid) > 0 {
		var oStarts [][2]int
		for c, cell := range grid[0] {
			if cell == 'O' {
				oStarts = append(oStarts, [2]int{0, c})
			}
		}
		if hasPath(grid, 'O', oStarts, func(r, _ int) bool { return r == len(grid)-1 }) {
			return "O", nil
		}
	}

	// Check X's path (left to right)
	if len(grid) > 0 && len(grid[0]) > 0 {
		var xStarts [][2]int
		for r := range grid {
			if len(grid[r]) == 0 {
				continue
			}
			if grid[r][0] == 'X' {
				xStarts = append(xStarts, [2]int{r, 0})
			}
		}
		if hasPath(grid, 'X', xStarts, func(_, c int) bool { return c == len(grid[0])-1 }) {
			return "X", nil
		}
	}

	return "", nil
}

func hasPath(grid [][]byte, player byte, starts [][2]int, isTarget func(int, int) bool) bool {
	if len(grid) == 0 || len(starts) == 0 {
		return false
	}

	visited := make([][]bool, len(grid))
	for i := range visited {
		visited[i] = make([]bool, len(grid[i]))
	}

	queue := make([][2]int, 0, len(starts))

	// Initialize queue with valid starting positions
	for _, pos := range starts {
		r, c := pos[0], pos[1]
		if r >= 0 && r < len(grid) && c >= 0 && c < len(grid[r]) && grid[r][c] == player {
			if !visited[r][c] {
				visited[r][c] = true
				queue = append(queue, [2]int{r, c})
			}
		}
	}

	directions := [][2]int{
		{-1, 0},  // up
		{-1, 1},  // up-right
		{0, -1},  // left
		{0, 1},   // right
		{1, -1},  // down-left
		{1, 0},   // down
	}

	for len(queue) > 0 {
		pos := queue[0]
		queue = queue[1:]
		r, c := pos[0], pos[1]

		if isTarget(r, c) {
			return true
		}

		for _, dir := range directions {
			nr, nc := r+dir[0], c+dir[1]
			if nr >= 0 && nr < len(grid) && nc >= 0 && nc < len(grid[nr]) {
				if !visited[nr][nc] && grid[nr][nc] == player {
					visited[nr][nc] = true
					queue = append(queue, [2]int{nr, nc})
				}
			}
		}
	}

	return false
}
