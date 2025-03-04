#include "yacht.h"
#include <algorithm>
#include <array>
#include <unordered_map>

namespace yacht {

namespace {
const std::unordered_map<std::string, Category> category_map = {
    {"ones", Category::Ones},
    {"twos", Category::Twos},
    {"threes", Category::Threes},
    {"fours", Category::Fours},
    {"fives", Category::Fives},
    {"sixes", Category::Sixes},
    {"full house", Category::FullHouse},
    {"four of a kind", Category::FourOfAKind},
    {"little straight", Category::LittleStraight},
    {"big straight", Category::BigStraight},
    {"choice", Category::Choice},
    {"yacht", Category::Yacht}
};

Category category_from_string(const std::string& str) {
    auto it = category_map.find(str);
    if (it != category_map.end()) {
        return it->second;
    }
    throw std::invalid_argument("Invalid category: " + str);
}

int score_impl(const std::vector<int>& dice, Category category) {
    // Count occurrences of each dice value
    std::unordered_map<int, int> counts;
    for (int die : dice) {
        counts[die]++;
    }

    // Helper functions
    auto sumDice = [&]() { return dice[0] + dice[1] + dice[2] + dice[3] + dice[4]; };
    auto countValue = [&](int value) { return counts[value] * value; };
    auto isStraight = [&](const std::array<int, 5>& straight) {
        std::array<int, 5> sortedDice{dice.begin(), dice.end()};
        std::sort(sortedDice.begin(), sortedDice.end());
        return sortedDice == straight;
    };

    switch (category) {
        case Category::Ones: return countValue(1);
        case Category::Twos: return countValue(2);
        case Category::Threes: return countValue(3);
        case Category::Fours: return countValue(4);
        case Category::Fives: return countValue(5);
        case Category::Sixes: return countValue(6);
        
        case Category::FullHouse: {
            bool hasThree = false, hasTwo = false;
            for (const auto& [value, count] : counts) {
                if (count == 3) hasThree = true;
                if (count == 2) hasTwo = true;
            }
            return (hasThree && hasTwo) ? sumDice() : 0;
        }
        
        case Category::FourOfAKind: {
            for (const auto& [value, count] : counts) {
                if (count >= 4) return 4 * value;
            }
            return 0;
        }
        
        case Category::LittleStraight:
            return isStraight({1, 2, 3, 4, 5}) ? 30 : 0;
            
        case Category::BigStraight:
            return isStraight({2, 3, 4, 5, 6}) ? 30 : 0;
            
        case Category::Choice:
            return sumDice();
            
        case Category::Yacht:
            return (counts.size() == 1) ? 50 : 0;
            
        default:
            return 0;
    }
}

} // namespace

int score(const std::vector<int>& dice, const std::string& category) {
    return score_impl(dice, category_from_string(category));
}

}  // namespace yacht
