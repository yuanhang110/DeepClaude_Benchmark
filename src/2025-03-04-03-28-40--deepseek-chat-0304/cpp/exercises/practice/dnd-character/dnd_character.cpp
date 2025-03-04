#include "dnd_character.h"
#include <numeric>
#include <algorithm>

namespace dnd_character {

int modifier(int constitution) {
    return (constitution - 10) / 2;
}

int ability() {
    static std::random_device rd;
    static std::mt19937 gen(rd());
    static std::uniform_int_distribution<> dis(1, 6);
    
    std::array<int, 4> rolls;
    for (int i = 0; i < 4; ++i) {
        rolls[i] = dis(gen);
    }
    
    // Sort and sum top 3
    std::sort(rolls.begin(), rolls.end(), std::greater<int>());
    return std::accumulate(rolls.begin(), rolls.begin() + 3, 0);
}

Character::Character() {
    for (int i = 0; i < 6; ++i) {
        abilities[i] = ability();
    }
    hitpoints_ = 10 + modifier(constitution());
}

}  // namespace dnd_character
