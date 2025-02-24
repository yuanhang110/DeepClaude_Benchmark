#include "robot_name.h"
#include <random>
#include <stdexcept>

namespace robot_name {

std::set<std::string> robot::used_names;
std::mt19937 robot::engine(std::random_device{}());

robot::robot() {
    name_ = generate_name();
}

void robot::reset() {
    used_names.erase(name_);
    name_ = generate_name();
}

[[maybe_unused]] std::string robot::generate_name() {
    std::uniform_int_distribution<int> letter_dist(0, 25);
    std::uniform_int_distribution<int> digit_dist(0, 9);
    
    while (true) {
        std::string name;
        
        // Generate two random letters
        name += 'A' + letter_dist(engine);
        name += 'A' + letter_dist(engine);
        
        // Generate three random digits
        for (int i = 0; i < 3; ++i) {
            name += '0' + digit_dist(engine);
        }
        
        // Check for uniqueness
        if (used_names.find(name) == used_names.end()) {
            used_names.insert(name);
            return name;
        }
        
        // If collision detected, try again
    }
}

}  // namespace robot_name
