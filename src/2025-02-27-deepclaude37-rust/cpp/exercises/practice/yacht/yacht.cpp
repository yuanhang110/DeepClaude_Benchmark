#include "yacht.h"
#include <algorithm>
#include <map>
#include <numeric>
#include <string>

namespace yacht {

int score(const std::vector<int>& dice, Category category) {
    std::vector<int> sorted(dice);
    std::sort(sorted.begin(), sorted.end());
    
    std::map<int, int> counts;
    for (int num : dice) {
        counts[num]++;
    }

    switch (category) {
        case Category::Ones:
            return counts[1] * 1;
        case Category::Twos:
            return counts[2] * 2;
        case Category::Threes:
            return counts[3] * 3;
        case Category::Fours:
            return counts[4] * 4;
        case Category::Fives:
            return counts[5] * 5;
        case Category::Sixes:
            return counts[6] * 6;
        case Category::FullHouse: {
            if (counts.size() != 2) return 0;
            auto it = counts.begin();
            int first_count = it->second;
            int second_count = (++it)->second;
            if ((first_count == 2 && second_count == 3) || (first_count == 3 && second_count == 2)) {
                return std::accumulate(dice.begin(), dice.end(), 0);
            }
            return 0;
        }
        case Category::FourOfAKind:
            for (const auto& pair : counts) {
                if (pair.second >= 4) {
                    return pair.first * 4;
                }
            }
            return 0;
        case Category::LittleStraight:
            return (sorted == std::vector<int>{1, 2, 3, 4, 5}) ? 30 : 0;
        case Category::BigStraight:
            return (sorted == std::vector<int>{2, 3, 4, 5, 6}) ? 30 : 0;
        case Category::Choice:
            return std::accumulate(dice.begin(), dice.end(), 0);
        case Category::Yacht:
            return (counts.size() == 1) ? 50 : 0;
        default:
            return 0;
    }
}

int score(const std::vector<int>& dice, const std::string& category_name) {
    if (category_name == "yacht") {
        return score(dice, Category::Yacht);
    } else if (category_name == "ones") {
        return score(dice, Category::Ones);
    } else if (category_name == "twos") {
        return score(dice, Category::Twos);
    } else if (category_name == "threes") {
        return score(dice, Category::Threes);
    } else if (category_name == "fours") {
        return score(dice, Category::Fours);
    } else if (category_name == "fives") {
        return score(dice, Category::Fives);
    } else if (category_name == "sixes") {
        return score(dice, Category::Sixes);
    } else if (category_name == "full house") {
        return score(dice, Category::FullHouse);
    } else if (category_name == "four of a kind") {
        return score(dice, Category::FourOfAKind);
    } else if (category_name == "little straight") {
        return score(dice, Category::LittleStraight);
    } else if (category_name == "big straight") {
        return score(dice, Category::BigStraight);
    } else if (category_name == "choice") {
        return score(dice, Category::Choice);
    }
    return 0; // Unknown category
}

}  // namespace yacht
