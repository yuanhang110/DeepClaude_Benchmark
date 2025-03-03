#include "robot_name.h"
#include <random>
#include <stdexcept>

namespace robot_name {

std::unordered_set<std::string> robot::used_names;

robot::robot() {
    reset();
}

void robot::reset() {
    if (!name_.empty()) {
        used_names.erase(name_);
    }
    
    if (used_names.size() >= MAX_NAMES) {
        throw std::runtime_error("All possible robot names are exhausted");
    }
    
    std::string new_name;
    do {
        new_name = generate_name();
    } while (!used_names.insert(new_name).second);
    name_ = new_name;
}

const std::string& robot::name() const {
    return name_;
}

std::string robot::generate_name() {
    static std::mt19937 engine(std::random_device{}());
    std::uniform_int_distribution<int> letter_dist(0, 25);
    std::uniform_int_distribution<int> digit_dist(0, 9);
    
    std::string name;
    name += 'A' + letter_dist(engine);
    name += 'A' + letter_dist(engine);
    name += '0' + digit_dist(engine);
    name += '0' + digit_dist(engine);
    name += '0' + digit_dist(engine);
    
    return name;
}

}  // namespace robot_name
