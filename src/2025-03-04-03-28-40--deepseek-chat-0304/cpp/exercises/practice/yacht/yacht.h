#pragma once
#include <vector>
#include <string>
#include <unordered_map>

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

Category category_from_string(const std::string& str);
int score(const std::vector<int>& dice, const std::string& category);

}  // namespace yacht
