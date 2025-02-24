from itertools import permutations

# Constants for the puzzle
COLORS = ['red', 'green', 'ivory', 'yellow', 'blue']
NATIONALITIES = ['Englishman', 'Spaniard', 'Ukrainian', 'Norwegian', 'Japanese']
PETS = ['dog', 'zebra', 'snail', 'fox', 'horse']
DRINKS = ['water', 'coffee', 'tea', 'milk', 'orange juice']
HOBBIES = ['dancing', 'painting', 'reading', 'football', 'chess']

def check_constraints(houses):
    colors, nations, pets, drinks, hobbies = houses
    
    # 2. The Englishman lives in the red house
    if colors[nations.index('Englishman')] != 'red':
        return False
    
    # 3. The Spaniard owns the dog
    if pets[nations.index('Spaniard')] != 'dog':
        return False
    
    # 4. Coffee is drunk in the green house
    if drinks[colors.index('green')] != 'coffee':
        return False
    
    # 5. The Ukrainian drinks tea
    if drinks[nations.index('Ukrainian')] != 'tea':
        return False
    
    # 6. The green house is immediately to the right of the ivory house
    if colors.index('green') != colors.index('ivory') + 1:
        return False
    
    # 7. The snail owner likes dancing
    if hobbies[pets.index('snail')] != 'dancing':
        return False
    
    # 8. Yellow house owner is a painter
    if hobbies[colors.index('yellow')] != 'painting':
        return False
    
    # 9. Milk is drunk in the middle house
    if drinks[2] != 'milk':
        return False
    
    # 10. Norwegian lives in the first house
    if nations[0] != 'Norwegian':
        return False
    
    # 11. Reader lives next to the fox owner
    reader_idx = hobbies.index('reading')
    fox_idx = pets.index('fox')
    if abs(reader_idx - fox_idx) != 1:
        return False
    
    # 12. Yellow house is next to horse owner
    yellow_idx = colors.index('yellow')
    horse_idx = pets.index('horse')
    if abs(yellow_idx - horse_idx) != 1:
        return False
    
    # 13. Football player drinks orange juice
    if drinks[hobbies.index('football')] != 'orange juice':
        return False
    
    # 14. Japanese person plays chess
    if hobbies[nations.index('Japanese')] != 'chess':
        return False
    
    # 15. Norwegian lives next to blue house
    if abs(nations.index('Norwegian') - colors.index('blue')) != 1:
        return False
    
    return True

def solve_puzzle():
    # Generate all possible permutations
    for nations in permutations(NATIONALITIES):
        # Skip if Norwegian is not in first house
        if nations[0] != 'Norwegian':
            continue
            
        for colors in permutations(COLORS):
            # Skip if green is not right of ivory
            if colors.index('green') != colors.index('ivory') + 1:
                continue
                
            # Skip if Englishman is not in red house
            if colors[nations.index('Englishman')] != 'red':
                continue
                
            for drinks in permutations(DRINKS):
                # Skip if milk is not in middle house
                if drinks[2] != 'milk':
                    continue
                    
                # Skip if Ukrainian doesn't drink tea
                if drinks[nations.index('Ukrainian')] != 'tea':
                    continue
                
                for pets in permutations(PETS):
                    # Skip if Spaniard doesn't own dog
                    if pets[nations.index('Spaniard')] != 'dog':
                        continue
                        
                    for hobbies in permutations(HOBBIES):
                        houses = (colors, nations, pets, drinks, hobbies)
                        if check_constraints(houses):
                            return houses
    return None

def drinks_water():
    """Return the nationality of the person who drinks water."""
    solution = solve_puzzle()
    if solution:
        colors, nations, pets, drinks, hobbies = solution
        return nations[drinks.index('water')]
    return None

def owns_zebra():
    """Return the nationality of the person who owns the zebra."""
    solution = solve_puzzle()
    if solution:
        colors, nations, pets, drinks, hobbies = solution
        return nations[pets.index('zebra')]
    return None
