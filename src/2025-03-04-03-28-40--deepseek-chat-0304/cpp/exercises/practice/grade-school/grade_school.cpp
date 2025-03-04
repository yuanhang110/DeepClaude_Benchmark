#include "grade_school.h"

namespace grade_school {

void school::add(const std::string& name, int grade) {
    // Check if student already exists in any grade
    for (const auto& [grd, students] : students_by_grade) {
        if (students.find(name) != students.end()) {
            throw std::runtime_error("Student already exists in grade " + std::to_string(grd));
        }
    }
    
    // Add student to the specified grade
    students_by_grade[grade].insert(name);
}

std::vector<std::string> school::grade(int grade) const {
    auto it = students_by_grade.find(grade);
    if (it == students_by_grade.end()) {
        return {};
    }
    
    return std::vector<std::string>(it->second.begin(), it->second.end());
}

std::map<int, std::vector<std::string>> school::roster() const {
    std::map<int, std::vector<std::string>> result;
    
    for (const auto& [grade, students] : students_by_grade) {
        result[grade] = std::vector<std::string>(students.begin(), students.end());
    }
    
    return result;
}

}  // namespace grade_school
