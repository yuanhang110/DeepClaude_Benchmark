package connect

import "strings"

func ResultOf(lines []string) (string, error) {
	// Handle empty input
	if len(lines) == 0 {
		return "", nil
	}

	// Create grid from input lines
	grid := make([][]rune, len(lines))
	for i, line := range lines {
		trimmed := strings.TrimSpace(line)
		grid[i] = []rune(trimmed)
	}

	// Check O's win (top to bottom)
	hasO := hasWinningPath(grid, 'O', isTopRow, isBottomRow)

	// Check X's win (left to right)
	hasX := hasWinningPath(grid, 'X', isLeftCol, isRightCol)

	// Return winner (O takes precedence if both somehow win)
	if hasO {
		return "O", nil
	}
	if hasX {
		return "X", nil
	}
	return "", nil
}

// Helper functions to check if a position is on an edge
func isTopRow(row, _ int) bool    { return row == 0 }
func isBottomRow(row, cols int) bool { return row == cols-1 }
func isLeftCol(_, col int) bool   { return col == 0 }
func isRightCol(_, col int) bool  { return col == col-1 }

// Checks if player has a winning path from their start edge to their target edge
func hasWinningPath(grid [][]rune, player rune, isStart, isTarget func(int, int) bool) bool {
	if len(grid) == 0 {
		return false
	}
	
	rows, cols := len(grid), len(grid[0])
	visited := make([][]bool, rows)
	for i := range visited {
		visited[i] = make([]bool, cols)
	}

	// Six possible neighbor directions in hex grid
	dirs := [][2]int{
		{-1, 0},  // up
		{1, 0},   // down 
		{0, -1},  // left
		{0, 1},   // right
		{-1, 1},  // up-right
		{1, -1},  // down-left
	}

	// Queue for BFS
	var queue [][2]int

	// Add all starting positions to queue
	for r := 0; r < rows; r++ {
		for c := 0; c < cols; c++ {
			if isStart(r, c) && grid[r][c] == player {
				queue = append(queue, [2]int{r, c})
				visited[r][c] = true
			}
		}
	}

	// BFS to find path
	for len(queue) > 0 {
		pos := queue[0]
		queue = queue[1:]
		r, c := pos[0], pos[1]

		// Check if we reached target edge
		if isTarget(r, c) {
			return true
		}

		// Check all neighbors
		for _, dir := range dirs {
			nr, nc := r+dir[0], c+dir[1]
			if nr >= 0 && nr < rows && nc >= 0 && nc < cols &&
				!visited[nr][nc] && grid[nr][nc] == player {
				visited[nr][nc] = true
				queue = append(queue, [2]int{nr, nc})
			}
		}
	}

	return false
}
