package connect

import "strings"

func ResultOf(lines []string) (string, error) {
	// Parse grid
	var grid [][]string
	for _, line := range lines {
		trimmed := strings.TrimSpace(line)
		if trimmed == "" {
			continue
		}
		grid = append(grid, strings.Fields(trimmed))
	}

	if len(grid) == 0 {
		return "", nil
	}

	// Check O's win (top to bottom)
	oVisited := make(map[[2]int]bool)
	var oQueue [][2]int
	for j, cell := range grid[0] {
		if cell == "O" {
			pos := [2]int{0, j}
			oQueue = append(oQueue, pos)
			oVisited[pos] = true
		}
	}

	for len(oQueue) > 0 {
		current := oQueue[0]
		oQueue = oQueue[1:]
		i, j := current[0], current[1]

		// Check if reached bottom row
		if i == len(grid)-1 {
			return "O", nil
		}

		// Get hexagonal directions based on row parity
		var dirs [][]int
		if i%2 == 0 {  // even row
			dirs = [][]int{{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, -1}}
		} else {  // odd row
			dirs = [][]int{{-1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0}}
		}
		
		for _, dir := range dirs {
			ni, nj := i+dir[0], j+dir[1]
			if ni >= 0 && ni < len(grid) && nj >= 0 && nj < len(grid[ni]) {
				pos := [2]int{ni, nj}
				if !oVisited[pos] && grid[ni][nj] == "O" {
					oVisited[pos] = true
					oQueue = append(oQueue, pos)
				}
			}
		}
	}

	// Check X's win (left to right)
	xVisited := make(map[[2]int]bool)
	var xQueue [][2]int
	for i, row := range grid {
		if len(row) == 0 {
			continue
		}
		if row[0] == "X" {
			pos := [2]int{i, 0}
			xQueue = append(xQueue, pos)
			xVisited[pos] = true
		}
	}

	for len(xQueue) > 0 {
		current := xQueue[0]
		xQueue = xQueue[1:]
		i, j := current[0], current[1]

		// Check if reached rightmost column
		if j == len(grid[i])-1 {
			return "X", nil
		}

		// Get hexagonal directions based on row parity
		var dirs [][]int
		if i%2 == 0 {  // even row
			dirs = [][]int{{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, -1}}
		} else {  // odd row
			dirs = [][]int{{-1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0}}
		}
		
		for _, dir := range dirs {
			ni, nj := i+dir[0], j+dir[1]
			if ni >= 0 && ni < len(grid) && nj >= 0 && nj < len(grid[ni]) {
				pos := [2]int{ni, nj}
				if !xVisited[pos] && grid[ni][nj] == "X" {
					xVisited[pos] = true
					xQueue = append(xQueue, pos)
				}
			}
		}
	}

	// No winner found
	return "", nil
}
