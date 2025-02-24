#include "zebra_puzzle.h"
#include <vector>
#include <algorithm>

using namespace std;

namespace zebra_puzzle {

// Helper function to generate permutations
template<typename T>
vector<vector<T>> permutations(const vector<T>& items) {
    vector<vector<T>> result;
    vector<T> copy(items);
    sort(copy.begin(), copy.end());
    do {
        result.push_back(copy);
    } while (next_permutation(copy.begin(), copy.end()));
    return result;
}

// Positional checks
bool RightOf(int a, int b) { return a == b + 1; }
bool NextTo(int a, int b) { return abs(a - b) == 1; }

Solution solve() {
    // Define all possible attributes
    const vector<string> nations = {"Englishman", "Spaniard", "Ukrainian", "Norwegian", "Japanese"};
    const vector<string> colors = {"Red", "Green", "Ivory", "Yellow", "Blue"};
    const vector<string> pets = {"Dog", "Snail", "Fox", "Horse", "Zebra"};
    const vector<string> drinks = {"Coffee", "Tea", "Milk", "OrangeJuice", "Water"};
    const vector<string> hobbies = {"Painting", "Dancing", "Reading", "Football", "Chess"};

    // Generate all possible permutations for each category
    for (const auto& nation : permutations(nations)) {
        // Clue 10: Norwegian lives in first house
        if (nation[0] != "Norwegian") continue;

        for (const auto& color : permutations(colors)) {
            // Clue 2: Englishman in Red house
            auto eng_house = find(nation.begin(), nation.end(), "Englishman") - nation.begin();
            if (color[eng_house] != "Red") continue;

            // Clue 6: Green house right of Ivory
            auto ivory = find(color.begin(), color.end(), "Ivory") - color.begin();
            auto green = find(color.begin(), color.end(), "Green") - color.begin();
            if (!RightOf(green, ivory)) continue;

            // Clue 15: Norwegian next to Blue
            auto blue = find(color.begin(), color.end(), "Blue") - color.begin();
            if (!NextTo(0, blue)) continue;

            for (const auto& drink : permutations(drinks)) {
                // Clue 9: Middle house drinks Milk
                if (drink[2] != "Milk") continue;

                // Clue 4: Green house drinks Coffee
                if (drink[green] != "Coffee") continue;

                // Clue 5: Ukrainian drinks Tea
                auto ukr = find(nation.begin(), nation.end(), "Ukrainian") - nation.begin();
                if (drink[ukr] != "Tea") continue;

                for (const auto& hobby : permutations(hobbies)) {
                    // Clue 8: Yellow house is painter
                    auto yellow = find(color.begin(), color.end(), "Yellow") - color.begin();
                    if (hobby[yellow] != "Painting") continue;

                    // Clue 13: Football player drinks OrangeJuice
                    auto football = find(hobby.begin(), hobby.end(), "Football") - hobby.begin();
                    if (drink[football] != "OrangeJuice") continue;

                    // Clue 14: Japanese plays Chess
                    auto japan = find(nation.begin(), nation.end(), "Japanese") - nation.begin();
                    if (hobby[japan] != "Chess") continue;

                    for (const auto& pet : permutations(pets)) {
                        // Clue 3: Spaniard owns Dog
                        auto spain = find(nation.begin(), nation.end(), "Spaniard") - nation.begin();
                        if (pet[spain] != "Dog") continue;

                        // Clue 7: Snail owner likes Dancing
                        auto snail = find(pet.begin(), pet.end(), "Snail") - pet.begin();
                        if (hobby[snail] != "Dancing") continue;

                        // Clue 11: Reading next to Fox
                        auto reading = find(hobby.begin(), hobby.end(), "Reading") - hobby.begin();
                        auto fox = find(pet.begin(), pet.end(), "Fox") - pet.begin();
                        if (!NextTo(reading, fox)) continue;

                        // Clue 12: Painter next to Horse
                        auto painter = find(hobby.begin(), hobby.end(), "Painting") - hobby.begin();
                        auto horse = find(pet.begin(), pet.end(), "Horse") - pet.begin();
                        if (!NextTo(painter, horse)) continue;

                        // Found valid solution
                        Solution sol;
                        auto water = find(drink.begin(), drink.end(), "Water") - drink.begin();
                        sol.drinksWater = nation[water];
                        auto zebra = find(pet.begin(), pet.end(), "Zebra") - pet.begin();
                        sol.ownsZebra = nation[zebra];
                        return sol;
                    }
                }
            }
        }
    }
    
    return {}; // Should never reach here with valid constraints
}

}  // namespace zebra_puzzle
