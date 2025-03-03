#include "dnd_character.h"
#include <vector>
#include <algorithm>
#include <random>

namespace dnd_character {

int ability() {
    static std::random_device rd;
    static std::mt19937 gen(rd());
    std::uniform_int_distribution<int> dist(1, 6);

    std::vector<int> rolls(4);
    for (auto& roll : rolls) {
        roll = dist(gen);
    }
    std::sort(rolls.begin(), rolls.end());
    // Sum the three highest dice (last three elements after sorting)
    return rolls[1] + rolls[2] + rolls[3];
}

int modifier(int ability) {
    int value = ability - 10;
    int mod = value / 2;
    // Handle negative odd numbers correctly
    if (value < 0 && (value % 2) != 0) {
        mod--;
    }
    return mod;
}

Character::Character()
    : strength(ability()),
      dexterity(ability()),
      constitution(ability()),
      intelligence(ability()),
      wisdom(ability()),
      charisma(ability()) {
    hitpoints = 10 + modifier(constitution);
}

}  // namespace dnd_character
