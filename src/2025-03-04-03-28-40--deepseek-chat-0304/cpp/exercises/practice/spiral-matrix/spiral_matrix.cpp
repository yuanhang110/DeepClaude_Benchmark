#include "spiral_matrix.h"
#include <vector>

namespace spiral_matrix {

std::vector<std::vector<int>> spiral_matrix(int size) {
    if (size <= 0) {
        return {};
    }

    std::vector<std::vector<int>> matrix(size, std::vector<int>(size));
    int num = 1;
    int left = 0, right = size - 1;
    int top = 0, bottom = size - 1;

    while (left <= right && top <= bottom) {
        // Traverse from left to right along top row
        for (int i = left; i <= right; i++) {
            matrix[top][i] = num++;
        }
        top++;

        // Traverse from top to bottom along right column
        for (int i = top; i <= bottom; i++) {
            matrix[i][right] = num++;
        }
        right--;

        // Traverse from right to left along bottom row
        if (top <= bottom) {
            for (int i = right; i >= left; i--) {
                matrix[bottom][i] = num++;
            }
            bottom--;
        }

        // Traverse from bottom to top along left column
        if (left <= right) {
            for (int i = bottom; i >= top; i--) {
                matrix[i][left] = num++;
            }
            left++;
        }
    }

    return matrix;
}

}  // namespace spiral_matrix
