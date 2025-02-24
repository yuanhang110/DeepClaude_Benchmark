#if !defined(GRADE_SCHOOL_H)
#define GRADE_SCHOOL_H

#include <string>
#include <vector>

namespace grade_school {

class school {
public:
    void add(const std::string& name, int grade);
    std::vector<std::string> roster() const;
    std::vector<std::string> grade(int grade_number) const;

private:
    struct Student {
        std::string name;
        int grade;
        
        bool operator<(const Student& other) const {
            return grade != other.grade ? grade < other.grade : name < other.name;
        }
    };
    std::vector<Student> students_;
};

}  // namespace grade_school

#endif // GRADE_SCHOOL_H
