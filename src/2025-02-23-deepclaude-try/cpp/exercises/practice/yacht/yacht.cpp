#include "yacht.h"

#include <algorithm>
#include <unordered_map>
#include <vector>

namespace yacht {

int score(const std::string& category, const std::vector<int>& dice) {
    std::unordered_map<int, int> counts_map;
    for (int die : dice) {
        counts_map[die]++;
    }

    std::vector<int> counts;
    for (const auto& pair : counts_map) {
        counts.push_back(pair.second);
    }
    std::sort(counts.rbegin(), counts.rend());

    std::vector<int> sorted_dice = dice;
    std::sort(sorted_dice.begin(), sorted_dice.end());

    // Handle number categories (ones through sixes)
    if (category == "ones") return std::count(dice.begin(), dice.end(), 1) * 1;
    if (category == "twos") return std::count(dice.begin(), dice.end(), 2) * 2;
    if (category == "threes") return std::count(dice.begin(), dice.end(), 3) * 3;
    if (category == "fours") return std::count(dice.begin(), dice.end(), 4) * 4;
    if (category == "fives") return std::count(dice.begin(), dice.end(), 5) * 5;
    if (category == "sixes") return std::count(dice.begin(), dice.end(), 6) * 6;

    if (category == "full house") {
        if (counts.size() == 2 && counts[0] == 3 && counts[1] == 2) {
            return std::accumulate(dice.begin(), dice.end(), 0);
        }
        return 0;
    }

    if (category == "four of a kind") {
        for (const auto& pair : counts_map) {
            if (pair.second >= 4) {
                return pair.first * 4;
            }
        }
        return 0;
    }

    if (category == "little straight") {
        if (sorted_dice == std::vector<int>{1, 2, 3, 4, 5}) {
            return 30;
        }
        return 0;
    }

    if (category == "big straight") {
        if (sorted_dice == std::vector<int>{2, 3, 4, 5, 6}) {
            return 30;
        }
        return 0;
    }

    if (category == "choice") {
        return std::accumulate(dice.begin(), dice.end(), 0);
    }

    if (category == "yacht") {
        if (counts.size() == 1 && counts[0] == 5) {
            return 50;
        }
        return 0;
    }

    return 0;
}

}  // namespace yacht
