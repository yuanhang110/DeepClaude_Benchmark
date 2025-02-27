#include "grade_school.h"

namespace grade_school {

void school::add(const std::string& name, int grade) {
    roster_[grade].insert(name);
}

std::vector<std::string> school::grade(int grade) const {
    auto it = roster_.find(grade);
    if (it == roster_.end()) {
        return {};
    }
    return std::vector<std::string>(it->second.begin(), it->second.end());
}

std::map<int, std::vector<std::string>> school::roster() const {
    std::map<int, std::vector<std::string>> result;
    for (const auto& [grade, students] : roster_) {
        result[grade] = std::vector<std::string>(students.begin(), students.end());
    }
    return result;
}

}  // namespace grade_school
