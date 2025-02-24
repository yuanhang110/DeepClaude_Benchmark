#include "grade_school.h"
#include <algorithm>

namespace grade_school {

void school::add(const std::string& name, int grade) {
    // Check if student already exists
    auto it = std::find_if(students_.begin(), students_.end(),
        [&name](const Student& s) { return s.name == name; });
    
    if (it == students_.end()) {
        students_.push_back({name, grade});
        std::sort(students_.begin(), students_.end());
    }
}

std::vector<std::string> school::roster() const {
    std::vector<std::string> result;
    result.reserve(students_.size());
    
    for (const auto& student : students_) {
        result.push_back(student.name);
    }
    
    return result;
}

std::vector<std::string> school::grade(int grade_number) const {
    std::vector<std::string> result;
    
    for (const auto& student : students_) {
        if (student.grade == grade_number) {
            result.push_back(student.name);
        }
    }
    
    std::sort(result.begin(), result.end());
    return result;
}

}  // namespace grade_school
