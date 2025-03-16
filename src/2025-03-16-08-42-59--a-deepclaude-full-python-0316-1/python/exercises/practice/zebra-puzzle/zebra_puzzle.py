from itertools import permutations

def drinks_water():
    """Returns the nationality of the person who drinks water."""
    houses = solve_zebra_puzzle()
    for house in houses:
        if house["drink"] == "water":
            return house["nationality"]

def owns_zebra():
    """Returns the nationality of the person who owns the zebra."""
    houses = solve_zebra_puzzle()
    for house in houses:
        if house["pet"] == "zebra":
            return house["nationality"]

def solve_zebra_puzzle():
    """
    Solves the zebra puzzle and returns the solution as a list of house dictionaries.
    Each house contains color, nationality, pet, drink, and hobby.
    """
    # Define all possible values for each attribute
    colors = ["red", "green", "ivory", "yellow", "blue"]
    nationalities = ["Englishman", "Spaniard", "Ukrainian", "Norwegian", "Japanese"]
    pets = ["dog", "snail", "fox", "horse", "zebra"]
    drinks = ["coffee", "tea", "milk", "orange juice", "water"]
    hobbies = ["painting", "dancing", "reading", "football", "chess"]
    
    # Try all possible arrangements of houses
    for color_perm in permutations(range(5)):
        # Map indices to actual values
        color = [colors[i] for i in color_perm]
        
        # Constraint 6: The green house is immediately to the right of the ivory house
        ivory_idx = -1
        green_idx = -1
        for i in range(5):
            if color[i] == "ivory":
                ivory_idx = i
            if color[i] == "green":
                green_idx = i
        if green_idx - ivory_idx != 1:
            continue
            
        for nationality_perm in permutations(range(5)):
            nationality = [nationalities[i] for i in nationality_perm]
            
            # Constraint 2: The Englishman lives in the red house
            if not any(nationality[i] == "Englishman" and color[i] == "red" for i in range(5)):
                continue
                
            # Constraint 10: The Norwegian lives in the first house
            if nationality[0] != "Norwegian":
                continue
                
            # Constraint 15: The Norwegian lives next to the blue house
            if not (color[1] == "blue"):
                continue
                
            for pet_perm in permutations(range(5)):
                pet = [pets[i] for i in pet_perm]
                
                # Constraint 3: The Spaniard owns the dog
                if not any(nationality[i] == "Spaniard" and pet[i] == "dog" for i in range(5)):
                    continue
                    
                for drink_perm in permutations(range(5)):
                    drink = [drinks[i] for i in drink_perm]
                    
                    # Constraint 4: Coffee is drunk in the green house
                    if not any(drink[i] == "coffee" and color[i] == "green" for i in range(5)):
                        continue
                        
                    # Constraint 5: The Ukrainian drinks tea
                    if not any(nationality[i] == "Ukrainian" and drink[i] == "tea" for i in range(5)):
                        continue
                        
                    # Constraint 9: Milk is drunk in the middle house
                    if drink[2] != "milk":
                        continue
                        
                    for hobby_perm in permutations(range(5)):
                        hobby = [hobbies[i] for i in hobby_perm]
                        
                        # Constraint 7: The snail owner likes dancing
                        if not any(pet[i] == "snail" and hobby[i] == "dancing" for i in range(5)):
                            continue
                            
                        # Constraint 8: The person in the yellow house is a painter
                        if not any(color[i] == "yellow" and hobby[i] == "painting" for i in range(5)):
                            continue
                            
                        # Constraint 11: The reader lives next to the fox owner
                        reader_idx = -1
                        fox_idx = -1
                        for i in range(5):
                            if hobby[i] == "reading":
                                reader_idx = i
                            if pet[i] == "fox":
                                fox_idx = i
                        if abs(reader_idx - fox_idx) != 1:
                            continue
                            
                        # Constraint 12: The painter's house is next to the house with the horse
                        painter_idx = -1
                        horse_idx = -1
                        for i in range(5):
                            if hobby[i] == "painting":
                                painter_idx = i
                            if pet[i] == "horse":
                                horse_idx = i
                        if abs(painter_idx - horse_idx) != 1:
                            continue
                            
                        # Constraint 13: The football player drinks orange juice
                        if not any(hobby[i] == "football" and drink[i] == "orange juice" for i in range(5)):
                            continue
                            
                        # Constraint 14: The Japanese person plays chess
                        if not any(nationality[i] == "Japanese" and hobby[i] == "chess" for i in range(5)):
                            continue
                            
                        # If we get here, we've found a solution that satisfies all constraints
                        houses = []
                        for i in range(5):
                            houses.append({
                                "position": i + 1,
                                "color": color[i],
                                "nationality": nationality[i],
                                "pet": pet[i],
                                "drink": drink[i],
                                "hobby": hobby[i]
                            })
                        return houses
    
    # Should never reach here if the puzzle has a solution
    return None
