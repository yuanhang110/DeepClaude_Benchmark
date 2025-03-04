#include "zebra_puzzle.h"
#include <algorithm>

namespace zebra_puzzle {

using Houses = std::array<House, 5>;

bool satisfies_constraints(const Houses& houses) {
    // Check all 15 constraints

    // 2. Englishman in red house
    auto englishman = find_house([](const House& h) {
        return h.nationality == Nationality::Englishman;
    });
    if (houses[englishman].color != Color::Red) return false;

    // 3. Spaniard owns dog
    auto spaniard = find_house([](const House& h) {
        return h.nationality == Nationality::Spaniard;
    });
    if (houses[spaniard].pet != Pet::Dog) return false;

    // 4. Green house drinks coffee
    auto green = find_house([](const House& h) { return h.color == Color::Green; });
    if (houses[green].drink != Drink::Coffee) return false;

    // 5. Ukrainian drinks tea
    auto ukrainian = find_house([](const House& h) {
        return h.nationality == Nationality::Ukrainian;
    });
    if (houses[ukrainian].drink != Drink::Tea) return false;

    // 6. Green house is immediately to the right of ivory
    auto ivory = find_house([](const House& h) { return h.color == Color::Ivory; });
    if (green != ivory + 1) return false;

    // 7. Snail owner likes dancing
    auto snail = find_house([](const House& h) { return h.pet == Pet::Snail; });
    if (houses[snail].hobby != Hobby::Dancing) return false;

    // 8. Yellow house is painter
    auto yellow = find_house([](const House& h) { return h.color == Color::Yellow; });
    if (houses[yellow].hobby != Hobby::Painting) return false;

    // 9. Middle house drinks milk
    if (houses[2].drink != Drink::Milk) return false;

    // 10. Norwegian in first house
    if (houses[0].nationality != Nationality::Norwegian) return false;

    // 11. Reader next to fox
    auto reader = find_house([](const House& h) { return h.hobby == Hobby::Reading; });
    auto fox = find_house([](const House& h) { return h.pet == Pet::Fox; });
    if (abs(reader - fox) != 1) return false;

    // 12. Painter next to horse
    auto painter = find_house([](const House& h) { return h.hobby == Hobby::Painting; });
    auto horse = find_house([](const House& h) { return h.pet == Pet::Horse; });
    if (abs(painter - horse) != 1) return false;

    // 13. Football player drinks orange juice
    auto football = find_house([](const House& h) { return h.hobby == Hobby::Football; });
    if (houses[football].drink != Drink::OrangeJuice) return false;

    // 14. Japanese plays chess
    auto japanese = find_house([](const House& h) {
        return h.nationality == Nationality::Japanese;
    });
    if (houses[japanese].hobby != Hobby::Chess) return false;

    // 15. Norwegian next to blue house
    auto norwegian = find_house([](const House& h) {
        return h.nationality == Nationality::Norwegian;
    });
    auto blue = find_house([](const House& h) { return h.color == Color::Blue; });
    if (abs(norwegian - blue) != 1) return false;

    return true;
}

Solution solve() {
    // Generate all possible permutations
    Houses houses;
    
    // Initialize houses with all possible values
    std::array<Color, 5> colors = {Color::Red, Color::Green, Color::Ivory, Color::Yellow, Color::Blue};
    std::array<Nationality, 5> nationalities = {Nationality::Englishman, Nationality::Spaniard, 
                                              Nationality::Ukrainian, Nationality::Norwegian, Nationality::Japanese};
    std::array<Pet, 5> pets = {Pet::Dog, Pet::Snail, Pet::Fox, Pet::Horse, Pet::Zebra};
    std::array<Drink, 5> drinks = {Drink::Coffee, Drink::Tea, Drink::Milk, Drink::OrangeJuice, Drink::Water};
    std::array<Hobby, 5> hobbies = {Hobby::Dancing, Hobby::Painting, Hobby::Reading, Hobby::Football, Hobby::Chess};

    // Try all combinations until we find one that satisfies all constraints
    do {
        for (int i = 0; i < 5; ++i) houses[i].color = colors[i];
        do {
            for (int i = 0; i < 5; ++i) houses[i].nationality = nationalities[i];
            do {
                for (int i = 0; i < 5; ++i) houses[i].pet = pets[i];
                do {
                    for (int i = 0; i < 5; ++i) houses[i].drink = drinks[i];
                    do {
                        for (int i = 0; i < 5; ++i) houses[i].hobby = hobbies[i];
                        if (satisfies_constraints(houses)) {
                            // Found solution, return it
                            Solution solution;
                            auto water = find_house(houses, [](const House& h) { return h.drink == Drink::Water; });
                            auto zebra = find_house(houses, [](const House& h) { return h.pet == Pet::Zebra; });
                            
                            switch (houses[water].nationality) {
                                case Nationality::Englishman: solution.drinksWater = "Englishman"; break;
                                case Nationality::Spaniard: solution.drinksWater = "Spaniard"; break;
                                case Nationality::Ukrainian: solution.drinksWater = "Ukrainian"; break;
                                case Nationality::Norwegian: solution.drinksWater = "Norwegian"; break;
                                case Nationality::Japanese: solution.drinksWater = "Japanese"; break;
                            }
                            
                            switch (houses[zebra].nationality) {
                                case Nationality::Englishman: solution.ownsZebra = "Englishman"; break;
                                case Nationality::Spaniard: solution.ownsZebra = "Spaniard"; break;
                                case Nationality::Ukrainian: solution.ownsZebra = "Ukrainian"; break;
                                case Nationality::Norwegian: solution.ownsZebra = "Norwegian"; break;
                                case Nationality::Japanese: solution.ownsZebra = "Japanese"; break;
                            }
                            
                            return solution;
                        }
                    } while (std::next_permutation(hobbies.begin(), hobbies.end()));
                } while (std::next_permutation(drinks.begin(), drinks.end()));
            } while (std::next_permutation(pets.begin(), pets.end()));
        } while (std::next_permutation(nationalities.begin(), nationalities.end()));
    } while (std::next_permutation(colors.begin(), colors.end()));

    return {}; // Should never reach here
}

}  // namespace zebra_puzzle
