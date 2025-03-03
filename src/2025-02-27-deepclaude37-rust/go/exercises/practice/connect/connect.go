package connect

import (
	"strings"
)

func ResultOf(lines []string) (string, error) {
	grid := parseGrid(lines)
	if checkOWins(grid) {
		return "O", nil
	}
	if checkXWins(grid) {
		return "X", nil
	}
	return "", nil
}

func parseGrid(lines []string) [][]rune {
	grid := make([][]rune, len(lines))
	for i, line := range lines {
		trimmed := strings.TrimSpace(line)
		cells := strings.Fields(trimmed)
		grid[i] = make([]rune, len(cells))
		for j, c := range cells {
			grid[i][j] = rune(c[0])
		}
	}
	return grid
}

func checkOWins(grid [][]rune) bool {
	rows := len(grid)
	if rows == 0 {
		return false
	}
	visited := make([][]bool, rows)
	for i := range visited {
		visited[i] = make([]bool, len(grid[i]))
	}

	queue := [][2]int{}

	// Add all O's in the top row
	for j := 0; j < len(grid[0]); j++ {
		if grid[0][j] == 'O' {
			queue = append(queue, [2]int{0, j})
			visited[0][j] = true
		}
	}

	for len(queue) > 0 {
		cell := queue[0]
		queue = queue[1:]
		r, c := cell[0], cell[1]

		if r == rows-1 {
			return true
		}

		// In a hex grid, neighbors depend on the row's parity
		var directions [][2]int
		if r%2 == 0 {
			// Even row
			directions = [][2]int{{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, 1}}
		} else {
			// Odd row
			directions = [][2]int{{-1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0}}
		}

		for _, dir := range directions {
			nr := r + dir[0]
			nc := c + dir[1]

			if nr >= 0 && nr < rows && nc >= 0 && nc < len(grid[nr]) {
				if !visited[nr][nc] && grid[nr][nc] == 'O' {
					visited[nr][nc] = true
					queue = append(queue, [2]int{nr, nc})
				}
			}
		}
	}

	return false
}

func checkXWins(grid [][]rune) bool {
	rows := len(grid)
	if rows == 0 {
		return false
	}

	visited := make([][]bool, rows)
	for i := range visited {
		visited[i] = make([]bool, len(grid[i]))
	}

	queue := [][2]int{}

	// Add all X's in the first column
	for i := 0; i < rows; i++ {
		if len(grid[i]) > 0 && grid[i][0] == 'X' {
			queue = append(queue, [2]int{i, 0})
			visited[i][0] = true
		}
	}

	for len(queue) > 0 {
		cell := queue[0]
		queue = queue[1:]
		r, c := cell[0], cell[1]

		if c == len(grid[r])-1 {
			return true
		}

		// In a hex grid, neighbors depend on the row's parity
		var directions [][2]int
		if r%2 == 0 {
			// Even row
			directions = [][2]int{{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, 1}}
		} else {
			// Odd row
			directions = [][2]int{{-1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0}}
		}

		for _, dir := range directions {
			nr := r + dir[0]
			nc := c + dir[1]

			if nr >= 0 && nr < rows && nc >= 0 && nc < len(grid[nr]) {
				if !visited[nr][nc] && grid[nr][nc] == 'X' {
					visited[nr][nc] = true
					queue = append(queue, [2]int{nr, nc})
				}
			}
		}
	}

	return false
}
