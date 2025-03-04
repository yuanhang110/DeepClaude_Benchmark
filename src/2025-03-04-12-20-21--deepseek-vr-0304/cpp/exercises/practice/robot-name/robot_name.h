#if !defined(ROBOT_NAME_H)
#define ROBOT_NAME_H

#include <string>
#include <unordered_set>

namespace robot_name {

class robot {
    std::string name;
    static std::unordered_set<std::string> used_names;
public:
    robot();
    std::string name() const;
    void reset();
};

}  // namespace robot_name

#endif // ROBOT_NAME_H
