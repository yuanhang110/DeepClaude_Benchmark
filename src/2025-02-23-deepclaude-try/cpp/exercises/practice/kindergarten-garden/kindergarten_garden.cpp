#include "kindergarten_garden.h"

#include <algorithm>
#include <stdexcept>
#include <vector>
#include <string>

namespace kindergarten_garden {

using namespace std;

namespace {

Plants convert_plant(char c) {
    switch (c) {
        case 'C': return Plants::clover;
        case 'G': return Plants::grass;
        case 'R': return Plants::radishes;
        case 'V': return Plants::violets;
        default: throw invalid_argument("Invalid plant code");
    }
}

} // namespace

vector<Plants> plants(const string& child, const string& diagram) {
    const vector<string> children = {
        "Alice", "Bob", "Charlie", "David",
        "Eve", "Fred", "Ginny", "Harriet",
        "Ileana", "Joseph", "Kincaid", "Larry"
    };
    
    auto it = find(children.begin(), children.end(), child);
    if (it == children.end()) {
        throw invalid_argument("Invalid child name");
    }
    int index = distance(children.begin(), it);
    
    // Split diagram into rows
    auto newline_pos = diagram.find('\n');
    if (newline_pos == string::npos) {
        throw invalid_argument("Invalid diagram format: missing newline");
    }
    
    string first_row = diagram.substr(0, newline_pos);
    string second_row = diagram.substr(newline_pos + 1);
    
    return {
        convert_plant(first_row[index * 2]),
        convert_plant(first_row[index * 2 + 1]),
        convert_plant(second_row[index * 2]),
        convert_plant(second_row[index * 2 + 1])
    };
}

}  // namespace kindergarten_garden
