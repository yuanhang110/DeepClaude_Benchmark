#include "kindergarten_garden.h"
#include <vector>
#include <string>
#include <algorithm>

namespace kindergarten_garden {

std::array<Plants, 4> plants(const std::string& diagram, const std::string& student) {
    // Declare the list of student names in alphabetical order
    const std::vector<std::string> students = {
        "Alice", "Bob", "Charlie", "David",
        "Eve", "Fred", "Ginny", "Harriet",
        "Ileana", "Joseph", "Kincaid", "Larry"
    };
    
    // Find the student's index
    auto it = std::find(students.begin(), students.end(), student);
    int student_index = std::distance(students.begin(), it);
    
    // Split the diagram into rows
    size_t newline_pos = diagram.find('\n');
    std::string row1 = diagram.substr(0, newline_pos);
    std::string row2 = diagram.substr(newline_pos + 1);
    
    // Get the starting position for this student's plants (2 per row)
    int start_pos = student_index * 2;
    
    // Map the plant codes to Plants enum values
    auto char_to_plant = [](char c) -> Plants {
        switch (c) {
            case 'V': return Plants::violets;
            case 'R': return Plants::radishes;
            case 'C': return Plants::clover;
            case 'G': return Plants::grass;
            default: return Plants::grass; // Fallback, should not happen
        }
    };
    
    // Return the 4 plants for this student
    return {
        char_to_plant(row1[start_pos]),     // First row, first plant
        char_to_plant(row1[start_pos + 1]), // First row, second plant
        char_to_plant(row2[start_pos]),     // Second row, first plant
        char_to_plant(row2[start_pos + 1])  // Second row, second plant
    };
}

}  // namespace kindergarten_garden
