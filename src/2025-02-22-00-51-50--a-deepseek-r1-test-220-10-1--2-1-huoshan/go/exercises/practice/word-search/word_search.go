package wordsearch

import "fmt"

func Solve(words []string, puzzle []string) (map[string][2][2]int, error) {
	result := make(map[string][2][2]int)
	directions := [][]int{
		{-1, 0},  // up
		{1, 0},   // down
		{0, -1},  // left
		{0, 1},   // right
		{-1, -1}, // up-left
		{-1, 1},  // up-right
		{1, -1},  // down-left
		{1, 1},   // down-right
	}

	for _, word := range words {
		if len(word) == 0 {
			continue
		}
		found := false
	wordLoop:
		for x, row := range puzzle {
			for y := range row {
				if row[y] != word[0] {
					continue
				}
				for _, dir := range directions {
					dx, dy := dir[0], dir[1]
					match := true
					for i := 0; i < len(word); i++ {
						nx := x + dx*i
						ny := y + dy*i
						if nx < 0 || nx >= len(puzzle) || ny < 0 || ny >= len(puzzle[nx]) {
							match = false
							break
						}
						if puzzle[nx][ny] != word[i] {
							match = false
							break
						}
					}
					if match {
						endX := x + dx*(len(word)-1)
						endY := y + dy*(len(word)-1)
						result[word] = [2][2]int{
							{y, x},
							{endY, endX},
						}
						found = true
						break wordLoop
					}
				}
			}
		}
		if !found {
			return nil, fmt.Errorf("word '%s' not found", word)
		}
	}
	return result, nil
}
