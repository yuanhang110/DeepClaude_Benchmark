#include "robot_name.h"
#include <random>
#include <algorithm>

namespace robot_name {

std::unordered_set<std::string> robot::used_names_;

robot::robot() {
    robot_name_ = generate_name();
}

std::string robot::name() const {
    return robot_name_;
}

void robot::reset() {
    used_names_.erase(robot_name_);
    robot_name_ = generate_name();
}

std::string robot::generate_name() {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> letter_dist(0, 25);
    std::uniform_int_distribution<> digit_dist(0, 9);

    std::string new_name;
    do {
        new_name = "";
        // Generate two random letters
        new_name += ('A' + letter_dist(gen));
        new_name += ('A' + letter_dist(gen));
        // Generate three random digits
        new_name += ('0' + digit_dist(gen));
        new_name += ('0' + digit_dist(gen));
        new_name += ('0' + digit_dist(gen));
    } while (used_names_.count(new_name) > 0);

    used_names_.insert(new_name);
    return new_name;
}

}  // namespace robot_name
