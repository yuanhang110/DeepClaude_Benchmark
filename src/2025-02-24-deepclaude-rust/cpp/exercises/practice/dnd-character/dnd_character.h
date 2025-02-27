#pragma once

#include <vector>

namespace dnd_character {

class DNDCharacter {
public:
    int strength;
    int dexterity;
    int constitution;
    int intelligence;
    int wisdom;
    int charisma;
    int hitpoints;

    DNDCharacter();

    static int modifier(int ability);
    static int ability();
};

using Character = DNDCharacter;

}  // namespace dnd_character
