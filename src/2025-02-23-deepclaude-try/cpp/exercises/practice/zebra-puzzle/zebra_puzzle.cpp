#include "zebra_puzzle.h"
#include <algorithm>
#include <array>

namespace zebra_puzzle {

using House = std::array<int, 5>;
constexpr int NUM_HOUSES = 5;

// Check if two positions are next to each other
bool is_next_to(int pos1, int pos2) {
    return std::abs(pos1 - pos2) == 1;
}

// Check if pos2 is immediately to the right of pos1
bool is_right_of(int pos1, int pos2) {
    return pos2 == pos1 + 1;
}

Solution solve() {
    House colors{0,1,2,3,4};     // red=0, green=1, ivory=2, yellow=3, blue=4
    House nations{0,1,2,3,4};    // english=0, spanish=1, ukrainian=2, norwegian=3, japanese=4
    House pets{0,1,2,3,4};       // dog=0, snail=1, fox=2, horse=3, zebra=4
    House drinks{0,1,2,3,4};     // coffee=0, tea=1, milk=2, orange juice=3, water=4
    House hobbies{0,1,2,3,4};    // dancing=0, reading=1, painting=2, football=3, chess=4

    // Constraint 10: Norwegian lives in first house
    do {
        if (nations[0] != 3) continue;
        
        // Constraint 9: Middle house drinks milk
        do {
            if (drinks[2] != 2) continue;
            
            do {
                do {
                    do {
                        // Apply all constraints
                        bool valid = true;
                        
                        // Constraint 2: Englishman lives in red house
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            if (nations[i] == 0 && colors[i] != 0) {
                                valid = false;
                                break;
                            }
                        }
                        
                        // Constraint 3: Spaniard owns dog
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            if (nations[i] == 1 && pets[i] != 0) {
                                valid = false;
                                break;
                            }
                        }
                        
                        // Constraint 4: Coffee is drunk in green house
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            if (colors[i] == 1 && drinks[i] != 0) {
                                valid = false;
                                break;
                            }
                        }
                        
                        // Constraint 5: Ukrainian drinks tea
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            if (nations[i] == 2 && drinks[i] != 1) {
                                valid = false;
                                break;
                            }
                        }
                        
                        // Constraint 6: Green house right of ivory
                        bool found = false;
                        for (int i = 0; i < NUM_HOUSES-1; i++) {
                            if (colors[i] == 2 && colors[i+1] == 1) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) valid = false;
                        
                        // Constraint 7: Snail owner likes dancing
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            if (pets[i] == 1 && hobbies[i] != 0) {
                                valid = false;
                                break;
                            }
                        }
                        
                        // Constraint 8: Yellow house owner is painter
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            if (colors[i] == 3 && hobbies[i] != 2) {
                                valid = false;
                                break;
                            }
                        }
                        
                        // Constraint 11: Reader lives next to fox owner
                        found = false;
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            for (int j = 0; j < NUM_HOUSES; j++) {
                                if (hobbies[i] == 1 && pets[j] == 2 && is_next_to(i,j)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) break;
                        }
                        if (!found) valid = false;
                        
                        // Constraint 12: Painter lives next to horse owner
                        found = false;
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            for (int j = 0; j < NUM_HOUSES; j++) {
                                if (hobbies[i] == 2 && pets[j] == 3 && is_next_to(i,j)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) break;
                        }
                        if (!found) valid = false;
                        
                        // Constraint 13: Football player drinks orange juice
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            if (hobbies[i] == 3 && drinks[i] != 3) {
                                valid = false;
                                break;
                            }
                        }
                        
                        // Constraint 14: Japanese plays chess
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            if (nations[i] == 4 && hobbies[i] != 4) {
                                valid = false;
                                break;
                            }
                        }
                        
                        // Constraint 15: Norwegian lives next to blue house
                        found = false;
                        for (int i = 0; i < NUM_HOUSES; i++) {
                            if (colors[i] == 4 && is_next_to(0,i)) { // Norwegian is at position 0
                                found = true;
                                break;
                            }
                        }
                        if (!found) valid = false;
                        
                        if (valid) {
                            Solution solution;
                            // Find who drinks water
                            for (int i = 0; i < NUM_HOUSES; i++) {
                                if (drinks[i] == 4) { // water
                                    switch(nations[i]) {
                                        case 0: solution.drinksWater = "Englishman"; break;
                                        case 1: solution.drinksWater = "Spaniard"; break;
                                        case 2: solution.drinksWater = "Ukrainian"; break;
                                        case 3: solution.drinksWater = "Norwegian"; break;
                                        case 4: solution.drinksWater = "Japanese"; break;
                                    }
                                }
                                if (pets[i] == 4) { // zebra
                                    switch(nations[i]) {
                                        case 0: solution.ownsZebra = "Englishman"; break;
                                        case 1: solution.ownsZebra = "Spaniard"; break;
                                        case 2: solution.ownsZebra = "Ukrainian"; break;
                                        case 3: solution.ownsZebra = "Norwegian"; break;
                                        case 4: solution.ownsZebra = "Japanese"; break;
                                    }
                                }
                            }
                            return solution;
                        }
                        
                    } while (std::next_permutation(hobbies.begin(), hobbies.end()));
                } while (std::next_permutation(pets.begin(), pets.end()));
            } while (std::next_permutation(colors.begin(), colors.end()));
        } while (std::next_permutation(drinks.begin(), drinks.end()));
    } while (std::next_permutation(nations.begin(), nations.end()));
    
    return Solution{"", ""};  // Should never reach here as puzzle has a unique solution
}

}  // namespace zebra_puzzle
