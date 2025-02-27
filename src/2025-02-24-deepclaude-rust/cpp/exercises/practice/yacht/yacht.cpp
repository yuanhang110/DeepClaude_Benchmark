#include "yacht.h"
#include <algorithm>
#include <numeric>
#include <vector>

namespace yacht {

int score(const std::vector<int>& dice, const std::string& category) {
    Category cat;
    if (category == "ones") cat = Category::Ones;
    else if (category == "twos") cat = Category::Twos;
    else if (category == "threes") cat = Category::Threes;
    else if (category == "fours") cat = Category::Fours;
    else if (category == "fives") cat = Category::Fives;
    else if (category == "sixes") cat = Category::Sixes;
    else if (category == "full house") cat = Category::FullHouse;
    else if (category == "four of a kind") cat = Category::FourOfAKind;
    else if (category == "little straight") cat = Category::LittleStraight;
    else if (category == "big straight") cat = Category::BigStraight;
    else if (category == "choice") cat = Category::Choice;
    else if (category == "yacht") cat = Category::Yacht;
    else return 0;

    switch (cat) {
        case Category::Ones: {
            int count = std::count(dice.begin(), dice.end(), 1);
            return count * 1;
        }
        case Category::Twos: {
            int count = std::count(dice.begin(), dice.end(), 2);
            return count * 2;
        }
        case Category::Threes: {
            int count = std::count(dice.begin(), dice.end(), 3);
            return count * 3;
        }
        case Category::Fours: {
            int count = std::count(dice.begin(), dice.end(), 4);
            return count * 4;
        }
        case Category::Fives: {
            int count = std::count(dice.begin(), dice.end(), 5);
            return count * 5;
        }
        case Category::Sixes: {
            int count = std::count(dice.begin(), dice.end(), 6);
            return count * 6;
        }
        case Category::FullHouse: {
            std::vector<int> counts(7, 0);
            for (int num : dice) {
                counts[num]++;
            }
            std::vector<int> non_zero;
            for (int i = 1; i <= 6; ++i) {
                if (counts[i] > 0) {
                    non_zero.push_back(counts[i]);
                }
            }
            std::sort(non_zero.rbegin(), non_zero.rend());
            if (non_zero.size() == 2 && non_zero[0] == 3 && non_zero[1] == 2) {
                return std::accumulate(dice.begin(), dice.end(), 0);
            }
            return 0;
        }
        case Category::FourOfAKind: {
            std::vector<int> counts(7, 0);
            for (int num : dice) {
                counts[num]++;
            }
            for (int i = 1; i <= 6; ++i) {
                if (counts[i] >= 4) {
                    return i * 4;
                }
            }
            return 0;
        }
        case Category::LittleStraight: {
            std::vector<int> sorted = dice;
            std::sort(sorted.begin(), sorted.end());
            return (sorted == std::vector<int>{1, 2, 3, 4, 5}) ? 30 : 0;
        }
        case Category::BigStraight: {
            std::vector<int> sorted = dice;
            std::sort(sorted.begin(), sorted.end());
            return (sorted == std::vector<int>{2, 3, 4, 5, 6}) ? 30 : 0;
        }
        case Category::Choice: {
            return std::accumulate(dice.begin(), dice.end(), 0);
        }
        case Category::Yacht: {
            bool all_same = std::all_of(dice.begin(), dice.end(), [&](int num) {
                return num == dice[0];
            });
            return all_same ? 50 : 0;
        }
        default: {
            return 0;
        }
    }
}

}  // namespace yacht
