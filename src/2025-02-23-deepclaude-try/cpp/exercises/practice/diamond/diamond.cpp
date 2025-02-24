#include "diamond.h"
#include <string>
#include <vector>

namespace diamond {

std::vector<std::string> rows(char c) {
    if (c == 'A') {
        return {"A"};
    }

    int max_distance = c - 'A';
    int size = 2 * max_distance + 1;
    std::vector<std::string> result;

    for (int i = 0; i <= 2 * max_distance; ++i) {
        int current_distance = i <= max_distance ? i : 2 * max_distance - i;
        char current_char = 'A' + current_distance;
        std::string row(size, ' ');

        int left = max_distance - current_distance;
        int right = max_distance + current_distance;
        row[left] = current_char;
        if (left != right) {
            row[right] = current_char;
        }

        result.push_back(row);
    }

    return result;
}

}  // namespace diamond
