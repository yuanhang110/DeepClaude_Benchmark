#include "dnd_character.h"
#include <vector>
#include <algorithm>
#include <random>
#include <cmath>

namespace dnd_character {

int ability() {
    static std::mt19937 gen(std::random_device{}());
    static std::uniform_int_distribution<> die(1, 6);

    std::vector<int> rolls;
    rolls.reserve(4);
    for (int i = 0; i < 4; ++i) {
        rolls.push_back(die(gen));
    }
    std::sort(rolls.begin(), rolls.end());
    return rolls[1] + rolls[2] + rolls[3];
}

Character::Character() {
    strength = ability();
    dexterity = ability();
    constitution = ability();
    intelligence = ability();
    wisdom = ability();
    charisma = ability();

    hitpoints = 10 + modifier(constitution);
}

int modifier(int constitution) {
    return static_cast<int>(std::floor((constitution - 10) / 2.0));
}

}  // namespace dnd_character
