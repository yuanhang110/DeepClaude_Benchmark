#if !defined(ROBOT_NAME_H)
#define ROBOT_NAME_H

#include <string>
#include <unordered_set>

namespace robot_name {

class robot {
public:
    Robot();
    std::string name() const;
    void reset();
    
private:
    std::string generate_name();
    std::string robot_name_;
    static std::unordered_set<std::string> used_names_;
};  // class robot
};

}  // namespace robot_name

#endif // ROBOT_NAME_H
