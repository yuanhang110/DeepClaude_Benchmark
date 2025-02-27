#if !defined(ROBOT_NAME_H)
#define ROBOT_NAME_H

#include <string>
#include <unordered_set>

namespace robot_name {

class Robot {
private:
    std::string name_;
    static std::unordered_set<std::string> used_names;

    void generate_name();

public:
    Robot();
    std::string name();
    void reset();
};

}  // namespace robot_name

#endif // ROBOT_NAME_H
