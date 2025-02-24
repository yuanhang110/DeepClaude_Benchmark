#include "dnd_character.h"
#include <random>
#include <algorithm>
#include <vector>
#include <cmath>

namespace dnd_character {

Character generate() {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> die(1, 6);

    Character c;
    c.strength = ability();
    c.dexterity = ability();
    c.constitution = ability();
    c.intelligence = ability();
    c.wisdom = ability();
    c.charisma = ability();

    c.hitpoints = 10 + modifier(c.constitution);

    return c;
}

int modifier(int constitution) {
    return static_cast<int>(std::floor((constitution - 10) / 2.0));
}

int ability() {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> die(1, 6);
    
    std::vector<int> rolls(4);
    for (auto& roll : rolls) {
        roll = die(gen);
    }
    std::sort(rolls.begin(), rolls.end(), std::greater<>());
    return rolls[0] + rolls[1] + rolls[2];
}

}  // namespace dnd_character
