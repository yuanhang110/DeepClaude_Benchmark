#if !defined(GRADE_SCHOOL_H)
#define GRADE_SCHOOL_H

#include <map>
#include <set>
#include <string>
#include <vector>

namespace grade_school {

class school {
    std::map<int, std::set<std::string>> roster_;
    
public:
    // Add student to grade, returns false if student already exists
    bool add(const std::string& name, int grade);
    
    // Get sorted roster of all students grouped by grade
    std::vector<std::string> roster() const;
    
    // Get sorted list of students for a specific grade
    std::set<std::string> grade(int grade) const;
};

}  // namespace grade_school

#endif // GRADE_SCHOOL_H
