package matrix

import (
	"errors"
	"strconv"
	"strings"
)

// Matrix represents a 2D array of integers
type Matrix [][]int

// New creates a Matrix from a string representation
func New(s string) (Matrix, error) {
	lines := strings.Split(s, "\n")
	matrix := make(Matrix, len(lines))
	
	for i, line := range lines {
		fields := strings.Fields(line)
		if i > 0 && len(fields) != len(matrix[0]) {
			return nil, errors.New("rows have unequal length")
		}
		
		row := make([]int, len(fields))
		for j, field := range fields {
			val, err := strconv.Atoi(field)
			if err != nil {
				return nil, err
			}
			row[j] = val
		}
		matrix[i] = row
	}
	
	return matrix, nil
}

// Rows returns a copy of the matrix's rows
func (m Matrix) Rows() [][]int {
	rows := make([][]int, len(m))
	for i, row := range m {
		rows[i] = make([]int, len(row))
		copy(rows[i], row)
	}
	return rows
}

// Cols returns a copy of the matrix's columns
func (m Matrix) Cols() [][]int {
	if len(m) == 0 {
		return nil
	}
	
	cols := make([][]int, len(m[0]))
	for i := range cols {
		cols[i] = make([]int, len(m))
		for j := range m {
			cols[i][j] = m[j][i]
		}
	}
	return cols
}

// Set updates the value at given row and column
func (m Matrix) Set(row, col, val int) bool {
	if row < 0 || row >= len(m) || col < 0 || col >= len(m[0]) {
		return false
	}
	m[row][col] = val
	return true
}
