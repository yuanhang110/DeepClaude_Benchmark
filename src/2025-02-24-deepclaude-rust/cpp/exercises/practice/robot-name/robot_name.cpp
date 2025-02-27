#include "robot_name.h"
#include <random>
#include <unordered_set>

namespace robot_name {

std::unordered_set<std::string> Robot::used_names;

Robot::Robot() : name_() {}

void Robot::generate_name() {
    static std::random_device rd;
    static std::mt19937 gen(rd());
    std::uniform_int_distribution<int> letter_dist(0, 25);
    std::uniform_int_distribution<int> digit_dist(0, 9);

    std::string new_name;
    do {
        new_name.clear();
        new_name += 'A' + letter_dist(gen);
        new_name += 'A' + letter_dist(gen);
        new_name += '0' + digit_dist(gen);
        new_name += '0' + digit_dist(gen);
        new_name += '0' + digit_dist(gen);
    } while (used_names.count(new_name));

    used_names.insert(new_name);
    name_ = new_name;
}

std::string Robot::name() {
    if (name_.empty()) {
        generate_name();
    }
    return name_;
}

void Robot::reset() {
    if (!name_.empty()) {
        used_names.erase(name_);
        name_.clear();
    }
}

}  // namespace robot_name
