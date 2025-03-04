#include "kindergarten_garden.h"
#include <algorithm>
#include <map>

namespace kindergarten_garden {

const std::vector<std::string> children = {
    "Alice", "Bob", "Charlie", "David",
    "Eve", "Fred", "Ginny", "Harriet",
    "Ileana", "Joseph", "Kincaid", "Larry"
};

const std::map<char, Plants> plant_map = {
    {'G', Plants::grass},
    {'C', Plants::clover},
    {'R', Plants::radishes},
    {'V', Plants::violets}
};

std::array<Plants, 4> plants(const std::string& diagram, const std::string& child) {
    std::string row1, row2;
    size_t newline = diagram.find('\n');
    row1 = diagram.substr(0, newline);
    row2 = diagram.substr(newline + 1);
    
    // Find child's index
    auto it = std::find(children.begin(), children.end(), child);
    if (it == children.end()) {
        return {};
    }
    size_t index = std::distance(children.begin(), it);
    size_t pos = index * 2;
    
    if (pos + 1 >= row1.length()) {
        return {};
    }
    
    return {
        plant_map.at(row1[pos]),
        plant_map.at(row1[pos + 1]),
        plant_map.at(row2[pos]),
        plant_map.at(row2[pos + 1])
    };
}

}  // namespace kindergarten_garden
