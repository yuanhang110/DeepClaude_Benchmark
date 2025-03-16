import itertools

def solution():
    """Solve the zebra puzzle and return the solution."""
    # Define houses by position (0-indexed)
    houses = range(5)
    
    # Generate all possible arrangements
    for colors in itertools.permutations(['red', 'green', 'ivory', 'yellow', 'blue']):
        # Constraint 6: The green house is immediately to the right of the ivory house
        if colors.index('green') != colors.index('ivory') + 1:
            continue
            
        for nationalities in itertools.permutations(['Englishman', 'Spaniard', 'Ukrainian', 'Norwegian', 'Japanese']):
            # Constraint 2: The Englishman lives in the red house
            if colors[nationalities.index('Englishman')] != 'red':
                continue
                
            # Constraint 10: The Norwegian lives in the first house
            if nationalities[0] != 'Norwegian':
                continue
                
            # Constraint 15: The Norwegian lives next to the blue house
            if abs(colors.index('blue') - nationalities.index('Norwegian')) != 1:
                continue
                
            for pets in itertools.permutations(['dog', 'snail', 'fox', 'horse', 'zebra']):
                # Constraint 3: The Spaniard owns the dog
                if pets[nationalities.index('Spaniard')] != 'dog':
                    continue
                    
                for drinks in itertools.permutations(['water', 'coffee', 'tea', 'milk', 'orange juice']):
                    # Constraint 4: The person in the green house drinks coffee
                    if drinks[colors.index('green')] != 'coffee':
                        continue
                        
                    # Constraint 5: The Ukrainian drinks tea
                    if drinks[nationalities.index('Ukrainian')] != 'tea':
                        continue
                        
                    # Constraint 9: The person in the middle house drinks milk
                    if drinks[2] != 'milk':
                        continue
                        
                    # Constraint 13: The person who plays football drinks orange juice
                    for hobbies in itertools.permutations(['painting', 'dancing', 'reading', 'football', 'chess']):
                        # Constraint 7: The snail owner likes to go dancing
                        if hobbies[pets.index('snail')] != 'dancing':
                            continue
                            
                        # Constraint 8: The person in the yellow house is a painter
                        if hobbies[colors.index('yellow')] != 'painting':
                            continue
                            
                        # Constraint 11: The person who enjoys reading lives in the house next to the person with the fox
                        if abs(hobbies.index('reading') - pets.index('fox')) != 1:
                            continue
                            
                        # Constraint 12: The painter's house is next to the house with the horse
                        if abs(hobbies.index('painting') - pets.index('horse')) != 1:
                            continue
                            
                        # Constraint 13: The person who plays football drinks orange juice
                        if drinks[hobbies.index('football')] != 'orange juice':
                            continue
                            
                        # Constraint 14: The Japanese person plays chess
                        if hobbies[nationalities.index('Japanese')] != 'chess':
                            continue
                        
                        # All constraints satisfied, we found the solution
                        water_drinker = nationalities[drinks.index('water')]
                        zebra_owner = nationalities[pets.index('zebra')]
                        
                        return water_drinker, zebra_owner
    
    # If no solution is found
    return None, None

def drinks_water():
    """Return the nationality of the person who drinks water."""
    return solution()[0]


def owns_zebra():
    """Return the nationality of the person who owns the zebra."""
    return solution()[1]
