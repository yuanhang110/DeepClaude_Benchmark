#include "spiral_matrix.h"

namespace spiral_matrix {
    std::vector<std::vector<int>> spiral_matrix(int size) {
        std::vector<std::vector<int>> matrix(size, std::vector<int>(size));
        
        int value = 1;
        int top = 0, bottom = size - 1;
        int left = 0, right = size - 1;
        
        while (value <= size * size) {
            // Fill top row
            for (int i = left; i <= right && value <= size * size; i++) {
                matrix[top][i] = value++;
            }
            top++;
            
            // Fill right column
            for (int i = top; i <= bottom && value <= size * size; i++) {
                matrix[i][right] = value++;
            }
            right--;
            
            // Fill bottom row
            for (int i = right; i >= left && value <= size * size; i--) {
                matrix[bottom][i] = value++;
            }
            bottom--;
            
            // Fill left column
            for (int i = bottom; i >= top && value <= size * size; i--) {
                matrix[i][left] = value++;
            }
            left++;
        }
        
        return matrix;
    }
}  // namespace spiral_matrix
