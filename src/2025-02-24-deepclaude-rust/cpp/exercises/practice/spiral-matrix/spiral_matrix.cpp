#include "spiral_matrix.h"

namespace spiral_matrix {

std::vector<std::vector<int>> spiral_matrix(int size) {
    std::vector<std::vector<int>> matrix(size, std::vector<int>(size));
    int num = 1;
    int top = 0, bottom = size - 1;
    int left = 0, right = size - 1;

    while (num <= size * size) {
        // Traverse from left to right on top row
        for (int i = left; i <= right; ++i) {
            matrix[top][i] = num++;
        }
        top++;

        // Traverse from top to bottom on right column
        for (int i = top; i <= bottom; ++i) {
            matrix[i][right] = num++;
        }
        right--;

        if (top <= bottom) {
            // Traverse from right to left on bottom row
            for (int i = right; i >= left; --i) {
                matrix[bottom][i] = num++;
            }
            bottom--;
        }

        if (left <= right) {
            // Traverse from bottom to top on left column
            for (int i = bottom; i >= top; --i) {
                matrix[i][left] = num++;
            }
            left++;
        }
    }

    return matrix;
}

}  // namespace spiral_matrix
