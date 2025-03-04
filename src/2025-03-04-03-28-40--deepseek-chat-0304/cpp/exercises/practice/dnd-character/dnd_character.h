#pragma once
#include <array>
#include <random>
#include <algorithm>

namespace dnd_character {

int modifier(int constitution);
int ability();

class Character {
public:
    Character();
    
    int strength() const { return abilities[0]; }
    int dexterity() const { return abilities[1]; }
    int constitution() const { return abilities[2]; }
    int intelligence() const { return abilities[3]; }
    int wisdom() const { return abilities[4]; }
    int charisma() const { return abilities[5]; }
    
    int hitpoints() const { return hitpoints_; }
    
private:
    std::array<int, 6> abilities;
    int hitpoints_;
};

}  // namespace dnd_character
