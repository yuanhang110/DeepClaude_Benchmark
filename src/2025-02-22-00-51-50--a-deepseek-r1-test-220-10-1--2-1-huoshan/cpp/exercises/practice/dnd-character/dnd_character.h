#pragma once

namespace dnd_character {

struct Character {
    int strength;
    int dexterity;
    int constitution;
    int intelligence;
    int wisdom;
    int charisma;
    int hitpoints;
};

int modifier(int constitution);
int ability();
Character generate();

}  // namespace dnd_character
