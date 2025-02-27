#if !defined(GRADE_SCHOOL_H)
#define GRADE_SCHOOL_H

#include <map>
#include <set>
#include <string>
#include <vector>

namespace grade_school {

class school {
public:
    void add(const std::string& name, int grade);
    std::map<int, std::vector<std::string>> roster() const;
    std::vector<std::string> grade(int grade) const;

private:
    std::map<int, std::set<std::string>> roster_;
};

}  // namespace grade_school

#endif // GRADE_SCHOOL_H
