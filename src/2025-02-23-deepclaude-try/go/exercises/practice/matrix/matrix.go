package matrix

import (
	"fmt"
	"strconv"
	"strings"
)

// Matrix represents a 2D grid of integers
type Matrix [][]int

func New(s string) (Matrix, error) {
	if len(s) == 0 {
		return nil, nil
	}

	var matrix Matrix
	rows := strings.Split(s, "\n")
	
	// Check for empty rows
	for i, row := range rows {
		if strings.TrimSpace(row) == "" {
			return nil, fmt.Errorf("row %d is empty", i)
		}
	}
	
	// Parse first row to determine column count
	firstRow := strings.Fields(rows[0])
	colCount := len(firstRow)
	
	for i, row := range rows {
		nums := strings.Fields(row)
		
		// Verify each row has same number of columns
		if len(nums) != colCount {
			return nil, fmt.Errorf("row %d has incorrect number of columns", i)
		}
		
		matrixRow := make([]int, colCount)
		for j, num := range nums {
			val, err := strconv.Atoi(num)
			if err != nil {
				return nil, fmt.Errorf("invalid integer at row %d, column %d", i, j)
			}
			matrixRow[j] = val
		}
		matrix = append(matrix, matrixRow)
	}
	
	return matrix, nil
}

// Cols returns a copy of the matrix columns
func (m Matrix) Cols() [][]int {
	if len(m) == 0 {
		return nil
	}
	
	cols := make([][]int, len(m[0]))
	for i := range cols {
		col := make([]int, len(m))
		for j := range m {
			col[j] = m[j][i]
		}
		cols[i] = col
	}
	return cols
}

func (m Matrix) Rows() [][]int {
	rows := make([][]int, len(m))
	for i, row := range m {
		rows[i] = make([]int, len(row))
		copy(rows[i], row)
	}
	return rows
}

func (m Matrix) Set(row, col, val int) bool {
	if row < 0 || row >= len(m) || col < 0 || col >= len(m[0]) {
		return false
	}
	m[row][col] = val
	return true
}
