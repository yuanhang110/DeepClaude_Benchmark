package wordsearch

import (
	"errors"
)

type direction struct {
	row, col int
}

var directions = []direction{
	{1, 0},   // down
	{1, 1},   // down-right
	{0, 1},   // right
	{-1, 1},  // up-right
	{-1, 0},  // up
	{-1, -1}, // up-left
	{0, -1},  // left
	{1, -1},  // down-left
}

func Solve(words []string, puzzle []string) (map[string][2][2]int, error) {
	result := make(map[string][2][2]int)
	rows := len(puzzle)
	if rows == 0 {
		return result, nil
	}
	cols := len(puzzle[0])

	for _, word := range words {
		found := false
		for i := 0; i < rows && !found; i++ {
			for j := 0; j < cols && !found; j++ {
				if puzzle[i][j] == word[0] {
					for _, dir := range directions {
						x, y := i, j
						matched := true
						for k := 1; k < len(word); k++ {
							x += dir.row
							y += dir.col
							if x < 0 || x >= rows || y < 0 || y >= cols || puzzle[x][y] != word[k] {
								matched = false
								break
							}
						}
						if matched {
							result[word] = [2][2]int{
								{j, i},
								{y, x},
							}
							found = true
							break
						}
					}
				}
			}
		}
		if !found {
			return nil, errors.New("word not found: " + word)
		}
	}

	return result, nil
}
