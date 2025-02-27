#include "dnd_character.h"
#include <vector>
#include <algorithm>
#include <random>

using namespace std;

namespace dnd_character {

DNDCharacter::DNDCharacter() :
    strength(ability()),
    dexterity(ability()),
    constitution(ability()),
    intelligence(ability()),
    wisdom(ability()),
    charisma(ability()),
    hitpoints(10 + modifier(constitution)) {}

int DNDCharacter::modifier(int ability) {
    int value = ability - 10;
    return (value < 0) ? (value - 1) / 2 : value / 2;
}

int DNDCharacter::ability() {
    vector<int> dice(4);
    static mt19937 engine(random_device{}());
    uniform_int_distribution<int> dist(1, 6);
    for (auto& die : dice) {
        die = dist(engine);
    }
    sort(dice.begin(), dice.end());
    return dice[1] + dice[2] + dice[3];
}

}  // namespace dnd_character
