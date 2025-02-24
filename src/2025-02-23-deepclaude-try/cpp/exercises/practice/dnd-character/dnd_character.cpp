#include "dnd_character.h"
#include <vector>
#include <algorithm>
#include <random>
#include <cmath>

namespace dnd_character {

int modifier(int ability_score) {
    return static_cast<int>(std::floor((ability_score - 10) / 2.0));
}

int ability() {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dis(1, 6);

    std::vector<int> rolls(4);
    for (auto& roll : rolls) {
        roll = dis(gen);
    }
    std::sort(rolls.begin(), rolls.end());
    return rolls[1] + rolls[2] + rolls[3];  // Sum the three highest rolls
}

Character generate() {
    Character ch;
    ch.strength = ability();
    ch.dexterity = ability();
    ch.constitution = ability();
    ch.intelligence = ability();
    ch.wisdom = ability();
    ch.charisma = ability();

    ch.hitpoints = 10 + modifier(ch.constitution);

    return ch;
}

}  // namespace dnd_character
