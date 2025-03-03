#if !defined(ROBOT_NAME_H)
#define ROBOT_NAME_H

#include <string>
#include <unordered_set>

namespace robot_name {

class robot {
    static const int MAX_NAMES = 26 * 26 * 1000;
    static std::unordered_set<std::string> used_names;
    std::string name_;
    
    static std::string generate_name();
public:
    robot();
    void reset();
    const std::string& name() const;
};

}  // namespace robot_name

#endif // ROBOT_NAME_H
