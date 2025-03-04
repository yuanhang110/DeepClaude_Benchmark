from itertools import permutations

# Define all possible attributes
colors = ['red', 'green', 'ivory', 'yellow', 'blue']
nationalities = ['Englishman', 'Spaniard', 'Ukrainian', 'Norwegian', 'Japanese']
pets = ['dog', 'snail', 'fox', 'horse', 'zebra']
drinks = ['coffee', 'tea', 'milk', 'orange juice', 'water']
hobbies = ['painter', 'dancing', 'reading', 'football', 'chess']

# Precompute all possible permutations
color_perms = list(permutations(colors))
nationality_perms = list(permutations(nationalities))
pet_perms = list(permutations(pets))
drink_perms = list(permutations(drinks))
hobby_perms = list(permutations(hobbies))

def is_valid_solution(colors, nationalities, pets, drinks, hobbies):
    # Constraint 2: Englishman lives in red house
    eng_idx = nationalities.index('Englishman')
    if colors[eng_idx] != 'red':
        return False
        
    # Constraint 3: Spaniard owns dog
    span_idx = nationalities.index('Spaniard')
    if pets[span_idx] != 'dog':
        return False
        
    # Constraint 4: Green house drinks coffee
    green_idx = colors.index('green')
    if drinks[green_idx] != 'coffee':
        return False
        
    # Constraint 5: Ukrainian drinks tea
    ukr_idx = nationalities.index('Ukrainian')
    if drinks[ukr_idx] != 'tea':
        return False
        
    # Constraint 6: Green house is immediately right of ivory
    ivory_idx = colors.index('ivory')
    if green_idx != ivory_idx + 1:
        return False
        
    # Constraint 7: Snail owner likes dancing
    snail_idx = pets.index('snail')
    if hobbies[snail_idx] != 'dancing':
        return False
        
    # Constraint 8: Yellow house is painter
    yellow_idx = colors.index('yellow')
    if hobbies[yellow_idx] != 'painter':
        return False
        
    # Constraint 9: Middle house drinks milk
    if drinks[2] != 'milk':
        return False
        
    # Constraint 10: Norwegian lives in first house
    if nationalities[0] != 'Norwegian':
        return False
        
    # Constraint 11: Reader next to fox
    reader_idx = hobbies.index('reading')
    fox_idx = pets.index('fox')
    if abs(reader_idx - fox_idx) != 1:
        return False
        
    # Constraint 12: Painter next to horse
    painter_idx = hobbies.index('painter')
    horse_idx = pets.index('horse')
    if abs(painter_idx - horse_idx) != 1:
        return False
        
    # Constraint 13: Football player drinks orange juice
    football_idx = hobbies.index('football')
    if drinks[football_idx] != 'orange juice':
        return False
        
    # Constraint 14: Japanese plays chess
    japan_idx = nationalities.index('Japanese')
    if hobbies[japan_idx] != 'chess':
        return False
        
    # Constraint 15: Norwegian next to blue house
    norway_idx = nationalities.index('Norwegian')
    blue_idx = colors.index('blue')
    if abs(norway_idx - blue_idx) != 1:
        return False
        
    return True

def find_solution():
    # Try all combinations until we find a valid one
    for colors in color_perms:
        for nationalities in nationality_perms:
            # Norwegian must be first (constraint 10)
            if nationalities[0] != 'Norwegian':
                continue
                
            for pets in pet_perms:
                for drinks in drink_perms:
                    # Middle house must drink milk (constraint 9)
                    if drinks[2] != 'milk':
                        continue
                        
                    for hobbies in hobby_perms:
                        if is_valid_solution(colors, nationalities, pets, drinks, hobbies):
                            return colors, nationalities, pets, drinks, hobbies
    return None

# Cache the solution
solution = find_solution()

def drinks_water():
    if not solution:
        return None
    colors, nationalities, pets, drinks, hobbies = solution
    water_idx = drinks.index('water')
    return nationalities[water_idx]

def owns_zebra():
    if not solution:
        return None
    colors, nationalities, pets, drinks, hobbies = solution
    zebra_idx = pets.index('zebra')
    return nationalities[zebra_idx]
