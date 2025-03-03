#include "diamond.h"
#include <vector>
#include <string>
#include <cmath>

namespace diamond {

std::vector<std::string> rows(char c) {
    if (c < 'A' || c > 'Z') {
        return {};
    }

    int max_level = c - 'A';
    int size = 2 * max_level + 1;
    std::vector<std::string> diamond;

    for (int row = 0; row < size; ++row) {
        int distance = std::abs(row - max_level);
        int current_level = max_level - distance;
        int leading_spaces = max_level - current_level;

        std::string line;
        line += std::string(leading_spaces, ' ');

        if (current_level == 0) {
            line += 'A';
        } else {
            char current_char = 'A' + current_level;
            line += current_char;
            line += std::string(2 * current_level - 1, ' ');
            line += current_char;
        }

        line += std::string(leading_spaces, ' ');
        diamond.push_back(line);
    }

    return diamond;
}

}  // namespace diamond
