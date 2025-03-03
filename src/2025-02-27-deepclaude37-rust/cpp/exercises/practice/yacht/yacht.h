#pragma once

#include <vector>
#include <string>

namespace yacht {

enum class Category {
    Ones,
    Twos,
    Threes,
    Fours,
    Fives,
    Sixes,
    FullHouse,
    FourOfAKind,
    LittleStraight,
    BigStraight,
    Choice,
    Yacht
};

int score(const std::vector<int>& dice, Category category);
int score(const std::vector<int>& dice, const std::string& category);

}  // namespace yacht
