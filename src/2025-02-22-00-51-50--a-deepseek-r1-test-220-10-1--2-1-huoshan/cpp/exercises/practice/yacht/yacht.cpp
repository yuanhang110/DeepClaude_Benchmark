#include "yacht.h"
#include <algorithm>
#include <map>
#include <numeric>

namespace yacht {

int score(std::array<int, 5> dice, const std::string& category) {
    if (category == "ones") {
        return std::count(dice.begin(), dice.end(), 1) * 1;
    } else if (category == "twos") {
        return std::count(dice.begin(), dice.end(), 2) * 2;
    } else if (category == "threes") {
        return std::count(dice.begin(), dice.end(), 3) * 3;
    } else if (category == "fours") {
        return std::count(dice.begin(), dice.end(), 4) * 4;
    } else if (category == "fives") {
        return std::count(dice.begin(), dice.end(), 5) * 5;
    } else if (category == "sixes") {
        return std::count(dice.begin(), dice.end(), 6) * 6;
    } else if (category == "full house") {
            std::map<int, int> counts;
            for (int die : dice) counts[die]++;
            if (counts.size() != 2) return 0;
            bool has3 = false, has2 = false;
            for (const auto& pair : counts) {
                if (pair.second == 3) has3 = true;
                else if (pair.second == 2) has2 = true;
                else return 0;
            }
            return (has3 && has2) ? std::accumulate(dice.begin(), dice.end(), 0) : 0;
    } else if (category == "four of a kind") {
            std::map<int, int> counts;
            for (int die : dice) counts[die]++;
            for (const auto& pair : counts) {
                if (pair.second >=4) return pair.first *4;
            }
            return 0;
    } else if (category == "little straight") {
            std::sort(dice.begin(), dice.end());
            return (dice == std::array<int,5>{1,2,3,4,5}) ? 30 : 0;
    } else if (category == "big straight") {
            std::sort(dice.begin(), dice.end());
            return (dice == std::array<int,5>{2,3,4,5,6}) ? 30 : 0;
    } else if (category == "choice") {
        return std::accumulate(dice.begin(), dice.end(), 0);
    } else if (category == "yacht") {
            std::map<int, int> counts;
            for (int die : dice) counts[die]++;
            return (counts.size() == 1) ? 50 : 0;
    }
    return 0;
}

}  // namespace yacht
