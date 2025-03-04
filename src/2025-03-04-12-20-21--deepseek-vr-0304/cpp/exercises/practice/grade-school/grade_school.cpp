#include "grade_school.h"

namespace grade_school {

bool school::add(const std::string& name, int grade) {
    auto& students = roster_[grade];
    return students.insert(name).second;
}

std::vector<std::string> school::grade(int grade) const {
    auto it = roster_.find(grade);
    if (it != roster_.end()) {
        return {it->second.begin(), it->second.end()};
    }
    return {};
}

std::map<int, std::vector<std::string>> school::roster() const {
    std::map<int, std::vector<std::string>> result;
    for (const auto& [gr, names] : roster_) {
        result[gr] = {names.begin(), names.end()};
    }
    return result;
}

}  // namespace grade_school
