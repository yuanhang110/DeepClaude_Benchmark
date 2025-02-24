#include "diamond.h"

namespace diamond {

std::vector<std::string> rows(char c) {
    if (c == 'A') return {"A"};  // Edge case
    
    const int size = (c - 'A') * 2 + 1;
    const int mid = size / 2;
    std::vector<std::string> diamond;
    
    // Build top half
    for (int row = 0; row <= mid; ++row) {
        std::string line(size, ' ');
        const char curr_char = 'A' + row;
        
        // Set left and right characters
        line[mid - row] = curr_char;
        line[mid + row] = curr_char;
        
        diamond.push_back(line);
    }
    
    // Mirror top half to create bottom (excluding middle row)
    for (int row = mid - 1; row >= 0; --row) {
        diamond.push_back(diamond[row]);
    }
    
    return diamond;
}

}  // namespace diamond
