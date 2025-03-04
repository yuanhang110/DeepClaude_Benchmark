#include "diamond.h"
#include <algorithm>
#include <string>
#include <vector>

using namespace std;

namespace diamond {

vector<string> rows(char letter) {
    if (letter < 'A' || letter > 'Z') {
        return {};
    }

    int size = letter - 'A' + 1;
    int width = 2 * size - 1;
    vector<string> diamond;

    // Generate top half including middle
    for (int i = 0; i < size; ++i) {
        char current_char = 'A' + i;
        int spaces = size - i - 1;
        string line(spaces, ' ');
        line += current_char;
        if (current_char != 'A') {
            line += string(2 * i - 1, ' ');
            line += current_char;
        }
        line += string(spaces, ' ');
        diamond.push_back(line);
    }

    // Generate bottom half by mirroring the top
    for (int i = size - 2; i >= 0; --i) {
        diamond.push_back(diamond[i]);
    }

    return diamond;
}

}  // namespace diamond
