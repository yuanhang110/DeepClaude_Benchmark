#include "robot_name.h"
#include <random>

namespace robot_name {

std::unordered_set<std::string> robot::used_names;

robot::robot() {}

std::string robot::name() const {
    if (name.empty()) {
        static std::mt19937 engine(std::random_device{}());
        std::uniform_int_distribution<int> letter_dist(0, 25);
        std::uniform_int_distribution<int> digit_dist(0, 9);
        std::string new_name;
        do {
            new_name.clear();
            new_name += 'A' + letter_dist(engine);
            new_name += 'A' + letter_dist(engine);
            new_name += '0' + digit_dist(engine);
            new_name += '0' + digit_dist(engine);
            new_name += '0' + digit_dist(engine);
        } while (used_names.count(new_name));
        used_names.insert(new_name);
        name = new_name;
    }
    return name;
}

void robot::reset() {
    if (!name.empty()) {
        used_names.erase(name);
        name.clear();
    }
}

}  // namespace robot_name
