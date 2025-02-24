#include "grade_school.h"

namespace grade_school {

bool school::add(const std::string& name, int grade) {
    // Check all grades for existing student
    for (const auto& [g, students] : roster_) {
        if (students.count(name)) {
            return false;
        }
    }
    
    roster_[grade].insert(name);
    return true;
}

std::vector<std::string> school::roster() const {
    std::vector<std::string> result;
    for (const auto& [grade, students] : roster_) {
        result.insert(result.end(), students.begin(), students.end());
    }
    return result;
}

std::set<std::string> school::grade(int grade) const {
    if (auto it = roster_.find(grade); it != roster_.end()) {
        return it->second;
    }
    return {};
}

}  // namespace grade_school
