#include "diamond.h"
#include <vector>
#include <string>

namespace diamond {

std::vector<std::string> rows(char c) {
    int n = c - 'A';
    std::vector<std::string> result;
    if (n < 0) return result;

    int size = 2 * n + 1;
    for (int i = 0; i < size; ++i) {
        int current = (i <= n) ? i : 2 * n - i;
        int leading = n - current;
        std::string line(leading, ' ');
        line += static_cast<char>('A' + current);
        if (current > 0) {
            line += std::string(2 * current - 1, ' ');
            line += static_cast<char>('A' + current);
        }
        result.push_back(line);
    }
    return result;
}

}  // namespace diamond
