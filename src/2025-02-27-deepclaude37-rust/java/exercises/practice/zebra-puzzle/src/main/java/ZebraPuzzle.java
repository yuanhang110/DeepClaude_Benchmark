import java.util.*;

class ZebraPuzzle {
    // Enum definitions for all house attributes
    private enum Color { RED, GREEN, IVORY, YELLOW, BLUE }
    private enum Nationality { ENGLISHMAN, SPANIARD, UKRAINIAN, NORWEGIAN, JAPANESE }
    private enum Drink { COFFEE, TEA, MILK, ORANGE_JUICE, WATER }
    private enum Pet { DOG, SNAILS, FOX, HORSE, ZEBRA }
    private enum Hobby { READING, PAINTING, DANCING, FOOTBALL, CHESS }

    // House class to represent each house in the puzzle
    private static class House {
        Color color;
        Nationality nationality;
        Drink drink;
        Pet pet;
        Hobby hobby;
        
        @Override
        public String toString() {
            return String.format("House{color=%s, nationality=%s, drink=%s, pet=%s, hobby=%s}",
                                color, nationality, drink, pet, hobby);
        }
    }

    // Solution cache
    private House[] solution;

    // Solve the puzzle and cache the solution
    private House[] solve() {
        if (solution != null) {
            return solution;
        }
        
        // Initialize houses
        House[] houses = new House[5];
        for (int i = 0; i < 5; i++) {
            houses[i] = new House();
        }
        
        // Apply fixed constraints
        // Clue 10: The Norwegian lives in the first house
        houses[0].nationality = Nationality.NORWEGIAN;
        
        // Clue 9: The person in the middle house drinks milk
        houses[2].drink = Drink.MILK;
        
        // Find the solution using backtracking
        if (solveDFS(houses, 0, 
                new HashSet<>(Arrays.asList(Color.values())), 
                new HashSet<>(Arrays.asList(Nationality.values())), 
                new HashSet<>(Arrays.asList(Drink.values())), 
                new HashSet<>(Arrays.asList(Pet.values())), 
                new HashSet<>(Arrays.asList(Hobby.values())))) {
            
            solution = houses;
            return houses;
        }
        
        throw new IllegalStateException("No solution found");
    }
    
    // Depth-first search with backtracking to find the solution
    private boolean solveDFS(House[] houses, int houseIndex, 
                           Set<Color> availableColors, 
                           Set<Nationality> availableNationalities,
                           Set<Drink> availableDrinks,
                           Set<Pet> availablePets,
                           Set<Hobby> availableHobbies) {
        
        // If all houses are assigned, check if the arrangement is valid
        if (houseIndex == 5) {
            return isValidArrangement(houses);
        }
        
        House house = houses[houseIndex];
        
        // Skip assignment for pre-set values
        if (houseIndex == 0) {  // Norwegian is in first house
            availableNationalities.remove(Nationality.NORWEGIAN);
        }
        if (houseIndex == 2) {  // Middle house drinks milk
            availableDrinks.remove(Drink.MILK);
        }
        
        // Assign color
        for (Color color : new ArrayList<>(availableColors)) {
            house.color = color;
            availableColors.remove(color);
            
            // Assign nationality
            if (houseIndex == 0) {
                house.nationality = Nationality.NORWEGIAN;
                assignRemaining(houses, houseIndex, availableColors, availableNationalities, 
                               availableDrinks, availablePets, availableHobbies);
            } else {
                for (Nationality nationality : new ArrayList<>(availableNationalities)) {
                    house.nationality = nationality;
                    availableNationalities.remove(nationality);
                    
                    assignRemaining(houses, houseIndex, availableColors, availableNationalities, 
                                   availableDrinks, availablePets, availableHobbies);
                    
                    availableNationalities.add(nationality);
                }
            }
            
            availableColors.add(color);
        }
        
        return false;
    }
    
    private boolean assignRemaining(House[] houses, int houseIndex,
                                 Set<Color> availableColors, 
                                 Set<Nationality> availableNationalities,
                                 Set<Drink> availableDrinks,
                                 Set<Pet> availablePets,
                                 Set<Hobby> availableHobbies) {
        
        House house = houses[houseIndex];
        
        // Assign drink
        if (houseIndex == 2) {
            house.drink = Drink.MILK;
            assignPetsAndHobbies(houses, houseIndex, availableColors, availableNationalities, 
                               availableDrinks, availablePets, availableHobbies);
        } else {
            for (Drink drink : new ArrayList<>(availableDrinks)) {
                house.drink = drink;
                availableDrinks.remove(drink);
                
                assignPetsAndHobbies(houses, houseIndex, availableColors, availableNationalities, 
                                   availableDrinks, availablePets, availableHobbies);
                
                availableDrinks.add(drink);
            }
        }
        
        return false;
    }
    
    private boolean assignPetsAndHobbies(House[] houses, int houseIndex,
                                      Set<Color> availableColors, 
                                      Set<Nationality> availableNationalities,
                                      Set<Drink> availableDrinks,
                                      Set<Pet> availablePets,
                                      Set<Hobby> availableHobbies) {
        
        House house = houses[houseIndex];
        
        // Assign pet
        for (Pet pet : new ArrayList<>(availablePets)) {
            house.pet = pet;
            availablePets.remove(pet);
            
            // Assign hobby
            for (Hobby hobby : new ArrayList<>(availableHobbies)) {
                house.hobby = hobby;
                availableHobbies.remove(hobby);
                
                // Check partial constraints
                if (isPartiallyValid(houses, houseIndex)) {
                    if (houseIndex == 4 || solveDFS(houses, houseIndex + 1, 
                                                availableColors, availableNationalities,
                                                availableDrinks, availablePets, availableHobbies)) {
                        return true;
                    }
                }
                
                availableHobbies.add(hobby);
            }
            
            availablePets.add(pet);
        }
        
        return false;
    }
    
    // Check constraints that can be verified with partially assigned houses
    private boolean isPartiallyValid(House[] houses, int currentIndex) {
        for (int i = 0; i <= currentIndex; i++) {
            House house = houses[i];
            
            // Check nationality-color constraints
            if (house.nationality == Nationality.ENGLISHMAN && house.color != Color.RED) {
                return false;
            }
            if (house.color == Color.RED && house.nationality != Nationality.ENGLISHMAN) {
                return false;
            }
            
            // Check nationality-pet constraints
            if (house.nationality == Nationality.SPANIARD && house.pet != Pet.DOG) {
                return false;
            }
            if (house.pet == Pet.DOG && house.nationality != Nationality.SPANIARD) {
                return false;
            }
            
            // Check color-drink constraints
            if (house.color == Color.GREEN && house.drink != Drink.COFFEE) {
                return false;
            }
            if (house.drink == Drink.COFFEE && house.color != Color.GREEN) {
                return false;
            }
            
            // Check nationality-drink constraints
            if (house.nationality == Nationality.UKRAINIAN && house.drink != Drink.TEA) {
                return false;
            }
            if (house.drink == Drink.TEA && house.nationality != Nationality.UKRAINIAN) {
                return false;
            }
            
            // Check pet-hobby constraints
            if (house.pet == Pet.SNAILS && house.hobby != Hobby.DANCING) {
                return false;
            }
            if (house.hobby == Hobby.DANCING && house.pet != Pet.SNAILS) {
                return false;
            }
            
            // Check color-hobby constraints
            if (house.color == Color.YELLOW && house.hobby != Hobby.PAINTING) {
                return false;
            }
            if (house.hobby == Hobby.PAINTING && house.color != Color.YELLOW) {
                return false;
            }
            
            // Check hobby-drink constraints
            if (house.hobby == Hobby.FOOTBALL && house.drink != Drink.ORANGE_JUICE) {
                return false;
            }
            if (house.drink == Drink.ORANGE_JUICE && house.hobby != Hobby.FOOTBALL) {
                return false;
            }
            
            // Check nationality-hobby constraints
            if (house.nationality == Nationality.JAPANESE && house.hobby != Hobby.CHESS) {
                return false;
            }
            if (house.hobby == Hobby.CHESS && house.nationality != Nationality.JAPANESE) {
                return false;
            }
            
            // Check the green-ivory constraint if both houses involved are assigned
            if (i > 0 && houses[i-1].color == Color.IVORY && house.color != Color.GREEN) {
                return false;
            }
            
            // Check adjacency constraints if previous/next houses are assigned
            if (i > 0) {
                House prevHouse = houses[i-1];
                
                // Check Norwegian-blue constraint
                if (house.nationality == Nationality.NORWEGIAN && prevHouse.color == Color.BLUE) {
                    return false;
                }
                if (prevHouse.nationality == Nationality.NORWEGIAN && house.color == Color.BLUE) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    // Check if a completed arrangement satisfies all constraints
    private boolean isValidArrangement(House[] houses) {
        // Check all previously checked constraints first
        if (!isPartiallyValid(houses, 4)) {
            return false;
        }
        
        // Clue 6: The green house is immediately to the right of the ivory house
        boolean ivoryGreenOk = false;
        for (int i = 0; i < 4; i++) {
            if (houses[i].color == Color.IVORY && houses[i+1].color == Color.GREEN) {
                ivoryGreenOk = true;
                break;
            }
        }
        if (!ivoryGreenOk) {
            return false;
        }
        
        // Clue 11: The person who enjoys reading lives in the house next to the person with the fox
        boolean readingFoxOk = false;
        for (int i = 0; i < 4; i++) {
            if ((houses[i].hobby == Hobby.READING && houses[i+1].pet == Pet.FOX) ||
                (houses[i].pet == Pet.FOX && houses[i+1].hobby == Hobby.READING)) {
                readingFoxOk = true;
                break;
            }
        }
        if (!readingFoxOk) {
            return false;
        }
        
        // Clue 12: The painter's house is next to the house with the horse
        boolean painterHorseOk = false;
        for (int i = 0; i < 4; i++) {
            if ((houses[i].hobby == Hobby.PAINTING && houses[i+1].pet == Pet.HORSE) ||
                (houses[i].pet == Pet.HORSE && houses[i+1].hobby == Hobby.PAINTING)) {
                painterHorseOk = true;
                break;
            }
        }
        if (!painterHorseOk) {
            return false;
        }
        
        // Clue 15: The Norwegian lives next to the blue house
        boolean norwegianBlueOk = false;
        for (int i = 0; i < 4; i++) {
            if ((houses[i].nationality == Nationality.NORWEGIAN && houses[i+1].color == Color.BLUE) ||
                (houses[i].color == Color.BLUE && houses[i+1].nationality == Nationality.NORWEGIAN)) {
                norwegianBlueOk = true;
                break;
            }
        }
        if (!norwegianBlueOk) {
            return false;
        }
        
        return true;
    }
    
    // Helper method to check if any house matches a condition
    private boolean hasMatch(House[] houses, java.util.function.Predicate<House> condition) {
        for (House house : houses) {
            if (condition.test(house)) {
                return true;
            }
        }
        return false;
    }
    
    // Helper method to check if any adjacent houses match a condition
    private boolean hasAdjacentMatch(House[] houses, java.util.function.BiPredicate<House, House> condition) {
        for (int i = 0; i < houses.length - 1; i++) {
            if (condition.test(houses[i], houses[i + 1])) {
                return true;
            }
        }
        return false;
    }
    
    // Generate all permutations of an array
    private <T> List<T[]> permute(T[] elements) {
        List<T[]> result = new ArrayList<>();
        generatePermutations(elements, 0, result);
        return result;
    }
    
    // Recursive helper for permutation generation
    private <T> void generatePermutations(T[] elements, int start, List<T[]> result) {
        if (start == elements.length - 1) {
            result.add(elements.clone());
            return;
        }
        
        for (int i = start; i < elements.length; i++) {
            // Swap elements
            T temp = elements[start];
            elements[start] = elements[i];
            elements[i] = temp;
            
            // Recurse on the remaining elements
            generatePermutations(elements, start + 1, result);
            
            // Restore the original order (backtrack)
            temp = elements[start];
            elements[start] = elements[i];
            elements[i] = temp;
        }
    }
    
    // Find the first house with a specific attribute value
    private <T> House findHouseWith(T attribute, java.util.function.Function<House, T> getter) {
        House[] houses = solve();
        for (House house : houses) {
            if (getter.apply(house).equals(attribute)) {
                return house;
            }
        }
        return null;
    }

    String getWaterDrinker() {
        House waterDrinker = findHouseWith(Drink.WATER, h -> h.drink);
        return waterDrinker.nationality.toString();
    }

    String getZebraOwner() {
        House zebraOwner = findHouseWith(Pet.ZEBRA, h -> h.pet);
        return zebraOwner.nationality.toString();
    }
}
