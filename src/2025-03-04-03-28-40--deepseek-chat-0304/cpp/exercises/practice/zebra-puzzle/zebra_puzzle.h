#if !defined(ZEBRA_PUZZLE_H)
#define ZEBRA_PUZZLE_H

#include <string>
#include <array>

namespace zebra_puzzle {

enum class Color { Red, Green, Ivory, Yellow, Blue };
enum class Nationality { Englishman, Spaniard, Ukrainian, Norwegian, Japanese };
enum class Pet { Dog, Snail, Fox, Horse, Zebra };
enum class Drink { Coffee, Tea, Milk, OrangeJuice, Water };
enum class Hobby { Dancing, Painting, Reading, Football, Chess };

struct House {
    Color color;
    Nationality nationality;
    Pet pet;
    Drink drink;
    Hobby hobby;
};

struct Solution {
    std::string drinksWater;
    std::string ownsZebra;
};

template<typename Predicate>
int find_house(const Houses& houses, Predicate pred) {
    return std::find_if(houses.begin(), houses.end(), pred) - houses.begin();
}

Solution solve();

}  // namespace zebra_puzzle

#endif  // ZEBRA_PUZZLE_H
