#include "spiral_matrix.h"

namespace spiral_matrix {

std::vector<std::vector<int>> spiral_matrix(int size) {
    std::vector<std::vector<int>> matrix(size, std::vector<int>(size));
    int num = 1;
    int start_row = 0, end_row = size - 1;
    int start_col = 0, end_col = size - 1;

    while (start_row <= end_row && start_col <= end_col) {
        // Top row
        for (int i = start_col; i <= end_col; ++i) {
            matrix[start_row][i] = num++;
        }
        start_row++;

        // Right column
        for (int i = start_row; i <= end_row; ++i) {
            matrix[i][end_col] = num++;
        }
        end_col--;

        // Bottom row
        if (start_row <= end_row) {
            for (int i = end_col; i >= start_col; --i) {
                matrix[end_row][i] = num++;
            }
            end_row--;
        }

        // Left column
        if (start_col <= end_col) {
            for (int i = end_row; i >= start_row; --i) {
                matrix[i][start_col] = num++;
            }
            start_col++;
        }
    }

    return matrix;
}

}  // namespace spiral_matrix
