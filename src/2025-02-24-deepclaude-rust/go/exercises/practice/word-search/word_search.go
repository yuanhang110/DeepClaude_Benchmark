package wordsearch

func Solve(words []string, puzzle []string) (map[string][2][2]int, error) {
	result := make(map[string][2][2]int)
	
	// Convert puzzle into a 2D array for easier access
	grid := make([][]rune, len(puzzle))
	for i, row := range puzzle {
		grid[i] = []rune(row)
	}
	
	// For each word, try to find it in the grid
	for _, word := range words {
		// Try each starting position
		for row := 0; row < len(grid); row++ {
			for col := 0; col < len(grid[0]); col++ {
				// Try each direction
				for _, dRow := range []int{-1, 0, 1} {
					for _, dCol := range []int{-1, 0, 1} {
						// Skip (0,0) direction as it's not a valid search direction
						if dRow == 0 && dCol == 0 {
							continue
						}
						
						// Check if word can fit in this direction
						endRow := row + dRow*(len(word)-1)
						endCol := col + dCol*(len(word)-1)
						
						// Check bounds
						if endRow >= 0 && endRow < len(grid) &&
							endCol >= 0 && endCol < len(grid[0]) {
							
							// Try to match word in this direction
							match := true
							for i := 0; i < len(word); i++ {
								checkRow := row + dRow*i
								checkCol := col + dCol*i
								if rune(word[i]) != grid[checkRow][checkCol] {
									match = false
									break
								}
							}
							
							if match {
								// Word found! Store coordinates of first and last letter
								result[word] = [2][2]int{{row, col}, {endRow, endCol}}
							}
						}
					}
				}
			}
		}
	}
	
	return result, nil
}
