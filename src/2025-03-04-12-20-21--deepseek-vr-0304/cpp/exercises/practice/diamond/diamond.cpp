#include "diamond.h"

using namespace std;

namespace diamond {

vector<string> rows(char letter) {
    vector<string> diamond;

    if (letter == 'A') {
        diamond.push_back("A");
        return diamond;
    }

    int num_rows = 2 * (letter - 'A') + 1;
    int middle_row = (num_rows - 1) / 2;

    for (int row = 0; row < num_rows; ++row) {
        int distance = abs(row - middle_row);
        char current_char = letter - distance;
        int current_pos = letter - 'A' - distance;
        int leading_spaces = (letter - 'A') - current_pos;

        string line;

        if (current_char == 'A') {
            line = string(leading_spaces, ' ') + "A" + string(leading_spaces, ' ');
        } else {
            int middle_spaces = 2 * current_pos - 1;
            line = string(leading_spaces, ' ') + current_char + string(middle_spaces, ' ') + current_char + string(leading_spaces, ' ');
        }

        diamond.push_back(line);
    }

    return diamond;
}

}  // namespace diamond
