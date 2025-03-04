#if !defined(GRADE_SCHOOL_H)
#define GRADE_SCHOOL_H

#include <map>
#include <set>
#include <stdexcept>
#include <string>
#include <vector>

namespace grade_school {

class school {
public:
    // Add a student to a grade
    void add(const std::string& name, int grade);
    
    // Get all students in a grade
    std::vector<std::string> grade(int grade) const;
    
    // Get all students in all grades, sorted by grade then name
    std::map<int, std::vector<std::string>> roster() const;

private:
    std::map<int, std::set<std::string>> students_by_grade;
};

}  // namespace grade_school

#endif // GRADE_SCHOOL_H
