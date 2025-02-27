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
	rows := strings.Split(s, "\n")
	data := make([][]int, len(rows))
	for i, row := range rows {
		fields := strings.Fields(row)
		dataRow := make([]int, len(fields))
		for j, field := range fields {
			val, err := strconv.Atoi(field)
			if err != nil {
				return nil, fmt.Errorf("invalid element: %q", field)
			}
			dataRow[j] = val
		}
		if i > 0 && len(dataRow) != len(data[0]) {
			return nil, fmt.Errorf("row %d has different length", i)
		}
		data[i] = dataRow
	}
	return &Matrix{data}, nil
}

// Cols and Rows must return the results without affecting the matrix.
func (m Matrix) Cols() [][]int {
	if len(m.data) == 0 {
		return nil
	}
	cols := make([][]int, len(m.data[0]))
	for i := range cols {
		cols[i] = make([]int, len(m.data))
		for j := 0; j < len(m.data); j++ {
			cols[i][j] = m.data[j][i]
		}
	}
	return cols
}

func (m Matrix) Rows() [][]int {
	rows := make([][]int, len(m.data))
	for i, row := range m.data {
		rows[i] = append([]int{}, row...)
	}
	return rows
}

func (m Matrix) Set(row, col, val int) bool {
	if row < 0 || row >= len(m.data) || col < 0 || col >= len(m.data[0]) {
		return false
	}
	m.data[row][col] = val
	return true
}
