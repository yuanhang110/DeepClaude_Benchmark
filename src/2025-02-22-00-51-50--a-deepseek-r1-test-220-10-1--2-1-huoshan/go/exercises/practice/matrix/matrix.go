package matrix

import (
	"fmt"
	"strconv"
	"strings"
)

type Matrix struct {
	data [][]int
}

func New(s string) (*Matrix, error) {
	lines := strings.Split(s, "\n")
	if len(lines) == 0 {
		return nil, fmt.Errorf("empty input")
	}

	data := make([][]int, len(lines))
	var cols int
	for i, line := range lines {
		fields := strings.Fields(line)
		if len(fields) == 0 {
			return nil, fmt.Errorf("row %d is empty", i)
		}
		if i == 0 {
			cols = len(fields)
		} else if len(fields) != cols {
			return nil, fmt.Errorf("row %d has inconsistent column count", i)
		}

		row := make([]int, len(fields))
		for j, field := range fields {
			val, err := strconv.Atoi(field)
			if err != nil {
				return nil, fmt.Errorf("invalid value %q at row %d column %d", field, i, j)
			}
			row[j] = val
		}
		data[i] = row
	}

	return &Matrix{data: data}, nil
}

// Cols and Rows must return the results without affecting the matrix.
func (m *Matrix) Cols() [][]int {
	if len(m.data) == 0 {
		return nil
	}
	cols := make([][]int, len(m.data[0]))
	for i := range cols {
		cols[i] = make([]int, len(m.data))
		for j := range m.data {
			cols[i][j] = m.data[j][i]
		}
	}
	return cols
}

func (m *Matrix) Rows() [][]int {
	rows := make([][]int, len(m.data))
	for i, row := range m.data {
		rows[i] = make([]int, len(row))
		copy(rows[i], row)
	}
	return rows
}

func (m *Matrix) Set(row, col, val int) bool {
	if row < 0 || row >= len(m.data) || col < 0 || col >= len(m.data[0]) {
		return false
	}
	m.data[row][col] = val
	return true
}
