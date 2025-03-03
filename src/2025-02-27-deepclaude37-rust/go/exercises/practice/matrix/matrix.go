package matrix

import (
	"errors"
	"fmt"
	"strconv"
	"strings"
)

type Matrix struct {
	rows [][]int
}

func New(s string) (*Matrix, error) {
	lines := strings.Split(s, "\n")
	rows := make([][]int, 0, len(lines))
	for _, line := range lines {
		line = strings.TrimSpace(line)
		if line == "" {
			continue
		}
		parts := strings.Fields(line)
		row := make([]int, len(parts))
		for i, part := range parts {
			num, err := strconv.Atoi(part)
			if err != nil {
				return nil, err
			}
			row[i] = num
		}
		rows = append(rows, row)
	}
	
	if len(rows) == 0 {
		return nil, errors.New("empty matrix")
	}
	
	// Check that all rows have the same length
	cols := len(rows[0])
	for i, row := range rows {
		if len(row) != cols {
			return nil, fmt.Errorf("row %d has %d columns, expected %d", i, len(row), cols)
		}
	}
	
	return &Matrix{rows: rows}, nil
}

// Cols and Rows must return the results without affecting the matrix.
func (m Matrix) Cols() [][]int {
	if len(m.rows) == 0 {
		return nil
	}
	
	numCols := len(m.rows[0])
	cols := make([][]int, numCols)
	
	for i := 0; i < numCols; i++ {
		col := make([]int, len(m.rows))
		for j := 0; j < len(m.rows); j++ {
			col[j] = m.rows[j][i]
		}
		cols[i] = col
	}
	
	return cols
}

func (m Matrix) Rows() [][]int {
	rows := make([][]int, len(m.rows))
	for i, row := range m.rows {
		rows[i] = make([]int, len(row))
		copy(rows[i], row)
	}
	return rows
}

func (m *Matrix) Set(row, col, val int) bool {
	if row < 0 || row >= len(m.rows) {
		return false
	}
	if col < 0 || len(m.rows) == 0 || col >= len(m.rows[0]) {
		return false
	}
	m.rows[row][col] = val
	return true
}
