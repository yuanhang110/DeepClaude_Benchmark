#include "spiral_matrix.h"

namespace spiral_matrix {

std::vector<std::vector<int>> spiral_matrix(int size) {
    std::vector<std::vector<int>> matrix;
    if (size <= 0) return matrix;

    matrix.resize(size, std::vector<int>(size, 0));

    int left = 0, right = size - 1;
    int top = 0, bottom = size - 1;
    int current = 1;

    while (left <= right && top <= bottom) {
        // Fill top row
        for (int col = left; col <= right; ++col) {
            matrix[top][col] = current++;
        }
        top++;

        // Fill right column
        for (int row = top; row <= bottom; ++row) {
            matrix[row][right] = current++;
        }
        right--;

        if (top <= bottom) {
            // Fill bottom row
            for (int col = right; col >= left; --col) {
                matrix[bottom][col] = current++;
            }
            bottom--;
        }

        if (left <= right) {
            // Fill left column
            for (int row = bottom; row >= top; --row) {
                matrix[row][left] = current++;
            }
            left++;
        }
    }

    return matrix;
}

}  // namespace spiral_matrix
