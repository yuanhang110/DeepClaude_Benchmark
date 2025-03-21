#if !defined(ZEBRA_PUZZLE_H)
#define ZEBRA_PUZZLE_H

#include <string>
#include <vector>
#include <array>

namespace zebra_puzzle {

struct Solution {
    std::string drinksWater;
    std::string ownsZebra;
};

Solution solve();

}  // namespace zebra_puzzle

#endif  // ZEBRA_PUZZLE_H
