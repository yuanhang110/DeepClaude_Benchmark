#include "kindergarten_garden.h"
#include <stdexcept>

namespace kindergarten_garden {

const std::vector<std::string> Garden::children_ = {
    "Alice", "Bob", "Charlie", "David",
    "Eve", "Fred", "Ginny", "Harriet",
    "Ileana", "Joseph", "Kincaid", "Larry"
};

Garden::Garden(const std::string& diagram) {
    size_t newline_pos = diagram.find('\n');
    if (newline_pos == std::string::npos || diagram.find('\n', newline_pos + 1) != std::string::npos) {
        throw std::invalid_argument("Invalid diagram format");
    }
    
    std::string row1 = diagram.substr(0, newline_pos);
    std::string row2 = diagram.substr(newline_pos + 1);
    
    cups_.push_back({row1.begin(), row1.end()});
    cups_.push_back({row2.begin(), row2.end()});
}

std::vector<Plants> Garden::plants(const std::string& child) const {
    int child_index = -1;
    for (size_t i = 0; i < children_.size(); ++i) {
        if (children_[i] == child) {
            child_index = i;
            break;
        }
    }
    
    if (child_index == -1) {
        return {};
    }
    
    std::vector<std::string> result;
    for (const auto& row : cups_) {
        if (row.size() < static_cast<size_t>(2 * child_index + 1)) {
            throw std::out_of_range("Invalid child index");
        }
        result.push_back(convert(row[2 * child_index]));
        result.push_back(convert(row[2 * child_index + 1]));
    }
    
    return result;
}

Plants Garden::convert(char c) const {
    switch (c) {
        case 'G': return Plants::grass;
        case 'C': return Plants::clover;
        case 'R': return Plants::radishes;
        case 'V': return Plants::violets;
        default: throw std::invalid_argument("Invalid plant code");
    }
}

}  // namespace kindergarten_garden
