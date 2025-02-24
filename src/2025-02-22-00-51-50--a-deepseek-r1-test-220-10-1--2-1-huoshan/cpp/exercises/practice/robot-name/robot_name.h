#if !defined(ROBOT_NAME_H)
#define ROBOT_NAME_H

#include <string>
#include <set>
#include <random>

namespace robot_name {

class robot {
public:
    Robot();
    void reset();
    std::string name() const { return name_; }

private:
    static std::set<std::string> used_names;
    static std::mt19937 engine;
    
    std::string name_;
    static std::string generate_name();
};

}  // namespace robot_name

#endif // ROBOT_NAME_H
