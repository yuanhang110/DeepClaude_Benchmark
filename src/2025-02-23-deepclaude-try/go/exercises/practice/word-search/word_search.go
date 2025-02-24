package wordsearch

import "fmt"

func Solve(words []string, puzzle []string) (map[string][2][2]int, error) {
	results := make(map[string][2][2]int)
	directions := []struct{ dx, dy int }{
		{1, 0},  // right
		{-1, 0}, // left
		{0, 1},  // down
		{0, -1}, // up
		{1, 1},  // down-right
		{-1, 1}, // down-left
		{1, -1}, // up-right
		{-1, -1}, // up-left
	}

	// Check for empty puzzle
	if len(puzzle) == 0 {
		return nil, fmt.Errorf("empty puzzle")
	}
	rows, cols := len(puzzle), len(puzzle[0])

	for _, word := range words {
		if len(word) == 0 {
			continue
		}
		found := false
		wordLength := len(word)

	WordSearch:
		for y := 0; y < rows; y++ {
			for x := 0; x < cols; x++ {
				for _, dir := range directions {
					// Calculate end position for current direction
					endX := x + (wordLength-1)*dir.dx
					endY := y + (wordLength-1)*dir.dy

					// Skip if word won't fit in this direction
					if endX < 0 || endX >= cols || endY < 0 || endY >= rows {
						continue
					}

					// Check each character
					matched := true
					for i := 0; i < wordLength; i++ {
						currX := x + i*dir.dx
						currY := y + i*dir.dy
						if puzzle[currY][currX] != word[i] {
							matched = false
							break
						}
					}

					if matched {
						results[word] = [2][2]int{{x, y}, {endX, endY}}
						found = true
						break WordSearch
					}
				}
			}
		}

		if !found {
			return nil, fmt.Errorf("word '%s' not found", word)
		}
	}

	return results, nil
}
