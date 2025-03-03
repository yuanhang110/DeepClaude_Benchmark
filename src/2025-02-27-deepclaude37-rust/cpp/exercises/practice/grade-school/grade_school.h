#if !defined(GRADE_SCHOOL_H)
#define GRADE_SCHOOL_H

#include <string>
#include <map>
#include <set>
#include <vector>

namespace grade_school {

class school {
public:
    void add(const std::string& name, int grade);
    std::vector<std::string> grade(int grade) const;
    std::vector<std::string> roster() const;

private:
    std::map<int, std::set<std::string>> students_by_grade_;
};

}  // namespace grade_school

#endif // GRADE_SCHOOL_H
