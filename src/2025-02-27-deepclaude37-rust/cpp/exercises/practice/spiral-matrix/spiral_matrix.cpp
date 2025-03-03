#include "spiral_matrix.h"

namespace spiral_matrix {

std::vector<std::vector<int>> spiral_matrix(int size) {
    std::vector<std::vector<int>> matrix(size, std::vector<int>(size));
    int num = 1;
    int top = 0, bottom = size - 1, left = 0, right = size - 1;

    while (top <= bottom && left <= right) {
        // Fill top row from left to right
        for (int i = left; i <= right; ++i) {
            matrix[top][i] = num++;
        }
        top++;

        // Fill right column from top to bottom
        for (int i = top; i <= bottom; ++i) {
            matrix[i][right] = num++;
        }
        right--;

        // Fill bottom row from right to left if there are rows left
        if (top <= bottom) {
            for (int i = right; i >= left; --i) {
                matrix[bottom][i] = num++;
            }
            bottom--;
        }

        // Fill left column from bottom to top if there are columns left
        if (left <= right) {
            for (int i = bottom; i >= top; --i) {
                matrix[i][left] = num++;
            }
            left++;
        }
    }

    return matrix;
}

}  // namespace spiral_matrix
