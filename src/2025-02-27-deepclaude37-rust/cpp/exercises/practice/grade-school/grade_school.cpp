#include "grade_school.h"

namespace grade_school {

void school::add(const std::string& name, int grade) {
    auto& students = students_by_grade_[grade];
    students.insert(name);
}

std::vector<std::string> school::grade(int grade) const {
    auto it = students_by_grade_.find(grade);
    if (it != students_by_grade_.end()) {
        return {it->second.begin(), it->second.end()};
    }
    return {};
}

std::vector<std::string> school::roster() const {
    std::vector<std::string> all_students;
    for (const auto& [grade_num, students] : students_by_grade_) {
        all_students.insert(all_students.end(), students.begin(), students.end());
    }
    return all_students;
}

}  // namespace grade_school
