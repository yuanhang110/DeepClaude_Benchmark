#include "kindergarten_garden.h"
#include <vector>
#include <string>
#include <algorithm>

namespace kindergarten_garden {

    std::array<Plants, 4> plants(const std::string &child, const std::string &diagram) {
        const std::vector<std::string> children_list = {
            "Alice", "Bob", "Charlie", "David",
            "Eve", "Fred", "Ginny", "Harriet",
            "Ileana", "Joseph", "Kincaid", "Larry"
        };

        auto it = std::find(children_list.begin(), children_list.end(), child);
        if (it == children_list.end()) {
            return {Plants::grass, Plants::grass, Plants::grass, Plants::grass}; // Default return
        }
        int index = it - children_list.begin();

        // Split the diagram into rows
        std::vector<std::string> rows;
        size_t pos = diagram.find('\n');
        if (pos != std::string::npos) {
            rows.push_back(diagram.substr(0, pos));
            rows.push_back(diagram.substr(pos + 1));
        } else {
            // If no newline, assume it's all in one row
            rows.push_back(diagram);
            rows.push_back("");  // Empty second row
        }

        const std::string &row1 = rows[0];
        const std::string &row2 = rows[1];

        std::string pair1 = row1.substr(index * 2, 2);
        std::string pair2 = row2.substr(index * 2, 2);

        std::string all_plants = pair1 + pair2;

        std::array<Plants, 4> result;
        for (int i = 0; i < 4 && i < all_plants.size(); i++) {
            char c = all_plants[i];
            switch (c) {
                case 'G': result[i] = Plants::grass; break;
                case 'C': result[i] = Plants::clover; break;
                case 'R': result[i] = Plants::radishes; break;
                case 'V': result[i] = Plants::violets; break;
                default: result[i] = Plants::grass; break; // Default to grass for any unexpected character
            }
        }

        return result;
    }

}  // namespace kindergarten_garden
