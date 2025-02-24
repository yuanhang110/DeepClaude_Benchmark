#include "robot_name.h"
#include <unordered_set>
#include <cstdlib>
#include <ctime>
#include <string>

namespace robot_name {

namespace {
    std::unordered_set<std::string> used_names;
    bool seeded = false;
}

robot::robot() {
    if (!seeded) {
        std::srand(std::time(nullptr));
        seeded = true;
    }
    generate_new_name();
}

std::string robot::name() const {
    return name_;
}

void robot::reset() {
    generate_new_name();
}

void robot::generate_new_name() {
    std::string new_name;
    do {
        // Generate two uppercase letters
        char letter1 = 'A' + std::rand() % 26;
        char letter2 = 'A' + std::rand() % 26;
        
        // Generate three digits
        int number = std::rand() % 1000;
        char digit1 = '0' + (number / 100) % 10;
        char digit2 = '0' + (number / 10) % 10;
        char digit3 = '0' + number % 10;
        
        new_name = {letter1, letter2, digit1, digit2, digit3};
    } while (used_names.count(new_name));

    // Remove old name from used_names if it exists
    if (!name_.empty()) {
        used_names.erase(name_);
    }

    name_ = new_name;
    used_names.insert(new_name);
}

}  // namespace robot_name
