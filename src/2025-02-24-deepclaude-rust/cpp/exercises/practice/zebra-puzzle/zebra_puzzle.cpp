#include "zebra_puzzle.h"
#include <array>
#include <vector>
#include <algorithm>

namespace zebra_puzzle {

Solution Solve() {
    // Define house positions (1-5)
    const int FIRST = 0;
    const int SECOND = 1;
    const int MIDDLE = 2;
    const int FOURTH = 3;
    const int FIFTH = 4;
    
    // Define attributes
    enum Color { RED, GREEN, IVORY, YELLOW, BLUE };
    enum Nationality { ENGLISH, SPANISH, UKRAINIAN, NORWEGIAN, JAPANESE };
    enum Pet { DOG, SNAIL, FOX, HORSE, ZEBRA };
    enum Drink { COFFEE, TEA, MILK, JUICE, WATER };
    enum Hobby { PAINTER, DANCING, CHESS, READING, FOOTBALL };
    
    // Create arrays to hold the final house configuration
    std::array<Color, 5> colors;
    std::array<Nationality, 5> nationalities;
    std::array<Pet, 5> pets;
    std::array<Drink, 5> drinks;
    std::array<Hobby, 5> hobbies;
    
    // Generate all possible permutations for testing
    std::vector<Color> colorPerm = {RED, GREEN, IVORY, YELLOW, BLUE};
    std::vector<Nationality> nationalityPerm = {ENGLISH, SPANISH, UKRAINIAN, NORWEGIAN, JAPANESE};
    std::vector<Pet> petPerm = {DOG, SNAIL, FOX, HORSE, ZEBRA};
    std::vector<Drink> drinkPerm = {COFFEE, TEA, MILK, JUICE, WATER};
    std::vector<Hobby> hobbyPerm = {PAINTER, DANCING, CHESS, READING, FOOTBALL};
    
    // Try all permutations to find a solution that satisfies all constraints
    do {
        colors = std::array<Color, 5>(colorPerm.begin(), colorPerm.end());
        
        do {
            nationalities = std::array<Nationality, 5>(nationalityPerm.begin(), nationalityPerm.end());
            
            // Constraint: The Norwegian lives in the first house
            if (nationalities[FIRST] != NORWEGIAN) continue;
            
            // Constraint: The Englishman lives in the red house
            if (nationalities[std::find(colors.begin(), colors.end(), RED) - colors.begin()] != ENGLISH) continue;
            
            // Constraint: The Norwegian lives next to the blue house
            if (!(colors[SECOND] == BLUE)) continue;
            
            do {
                pets = std::array<Pet, 5>(petPerm.begin(), petPerm.end());
                
                // Constraint: The Spaniard owns the dog
                if (pets[std::find(nationalities.begin(), nationalities.end(), SPANISH) - nationalities.begin()] != DOG) continue;
                
                do {
                    drinks = std::array<Drink, 5>(drinkPerm.begin(), drinkPerm.end());
                    
                    // Constraint: The person in the green house drinks coffee
                    if (drinks[std::find(colors.begin(), colors.end(), GREEN) - colors.begin()] != COFFEE) continue;
                    
                    // Constraint: The Ukrainian drinks tea
                    if (drinks[std::find(nationalities.begin(), nationalities.end(), UKRAINIAN) - nationalities.begin()] != TEA) continue;
                    
                    // Constraint: The person in the middle house drinks milk
                    if (drinks[MIDDLE] != MILK) continue;
                    
                    do {
                        hobbies = std::array<Hobby, 5>(hobbyPerm.begin(), hobbyPerm.end());
                        
                        // Constraint: The snail owner likes to go dancing
                        if (hobbies[std::find(pets.begin(), pets.end(), SNAIL) - pets.begin()] != DANCING) continue;
                        
                        // Constraint: The person in the yellow house is a painter
                        if (hobbies[std::find(colors.begin(), colors.end(), YELLOW) - colors.begin()] != PAINTER) continue;
                        
                        // Constraint: The person who plays football drinks orange juice
                        if (drinks[std::find(hobbies.begin(), hobbies.end(), FOOTBALL) - hobbies.begin()] != JUICE) continue;
                        
                        // Constraint: The Japanese person plays chess
                        if (hobbies[std::find(nationalities.begin(), nationalities.end(), JAPANESE) - nationalities.begin()] != CHESS) continue;
                        
                        // Constraint: The green house is immediately to the right of the ivory house
                        bool greenRightOfIvory = false;
                        for (int i = 0; i < 4; i++) {
                            if (colors[i] == IVORY && colors[i+1] == GREEN) {
                                greenRightOfIvory = true;
                                break;
                            }
                        }
                        if (!greenRightOfIvory) continue;
                        
                        // Constraint: The person who enjoys reading lives in the house next to the person with the fox
                        int foxPos = std::find(pets.begin(), pets.end(), FOX) - pets.begin();
                        int readingPos = std::find(hobbies.begin(), hobbies.end(), READING) - hobbies.begin();
                        bool readerNextToFox = abs(foxPos - readingPos) == 1;
                        if (!readerNextToFox) continue;
                        
                        // Constraint: The painter's house is next to the house with the horse
                        int painterPos = std::find(hobbies.begin(), hobbies.end(), PAINTER) - hobbies.begin();
                        int horsePos = std::find(pets.begin(), pets.end(), HORSE) - pets.begin();
                        bool painterNextToHorse = abs(painterPos - horsePos) == 1;
                        if (!painterNextToHorse) continue;
                        
                        // If we get here, all constraints are satisfied - we found a solution!
                        std::string drinksWater;
                        std::string ownsZebra;
                        
                        int waterDrinker = std::find(drinks.begin(), drinks.end(), WATER) - drinks.begin();
                        int zebraOwner = std::find(pets.begin(), pets.end(), ZEBRA) - pets.begin();
                        
                        switch (nationalities[waterDrinker]) {
                            case ENGLISH: drinksWater = "Englishman"; break;
                            case SPANISH: drinksWater = "Spaniard"; break;
                            case UKRAINIAN: drinksWater = "Ukrainian"; break;
                            case NORWEGIAN: drinksWater = "Norwegian"; break;
                            case JAPANESE: drinksWater = "Japanese"; break;
                        }
                        
                        switch (nationalities[zebraOwner]) {
                            case ENGLISH: ownsZebra = "Englishman"; break;
                            case SPANISH: ownsZebra = "Spaniard"; break;
                            case UKRAINIAN: ownsZebra = "Ukrainian"; break;
                            case NORWEGIAN: ownsZebra = "Norwegian"; break;
                            case JAPANESE: ownsZebra = "Japanese"; break;
                        }
                        
                        return Solution{drinksWater, ownsZebra};
                    } while (std::next_permutation(hobbyPerm.begin(), hobbyPerm.end()));
                } while (std::next_permutation(drinkPerm.begin(), drinkPerm.end()));
            } while (std::next_permutation(petPerm.begin(), petPerm.end()));
        } while (std::next_permutation(nationalityPerm.begin(), nationalityPerm.end()));
    } while (std::next_permutation(colorPerm.begin(), colorPerm.end()));
    
    // Should never reach here if the puzzle has a unique solution
    return Solution{"", ""};
}

}  // namespace zebra_puzzle
