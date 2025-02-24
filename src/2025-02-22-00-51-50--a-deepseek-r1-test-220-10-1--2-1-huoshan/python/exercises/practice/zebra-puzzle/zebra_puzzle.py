import itertools

def drinks_water():
    return next(solution)["drinks_water"]

def owns_zebra():
    return next(solution)["owns_zebra"]

def solution():
    # Define all possible attributes
    colors = ['red', 'green', 'ivory', 'yellow', 'blue']
    nations = ['English', 'Spaniard', 'Ukrainian', 'Norwegian', 'Japanese']
    pets = ['dog', 'snail', 'fox', 'horse', 'zebra']
    drinks = ['coffee', 'tea', 'milk', 'orange juice', 'water']
    hobbies = ['painter', 'dancing', 'reading', 'football', 'chess']

    # Generate all possible permutations with early constraint filtering
    for color in itertools.permutations(colors):
        # Green is immediately right of ivory (clue 6)
        if color.index("green") - color.index("ivory") != 1:
            continue

        for nation in itertools.permutations(nations):
            # Norwegian in first house (clue 10)
            if nation[0] != 'Norwegian':
                continue
            # English in red house (clue 2)
            if color[nation.index('English')] != 'red':
                continue

            for drink in itertools.permutations(drinks):
                # Milk in middle house (clue 9)
                if drink[2] != 'milk':
                    continue
                # Coffee in green house (clue 4)
                if drink[color.index('green')] != 'coffee':
                    continue
                # Ukrainian drinks tea (clue 5)
                if drink[nation.index('Ukrainian')] != 'tea':
                    continue

                for hobby in itertools.permutations(hobbies):
                    # Football drinks orange juice (clue 13)
                    if drink[hobby.index('football')] != 'orange juice':
                        continue
                    # Japanese plays chess (clue 14)
                    if hobby[nation.index('Japanese')] != 'chess':
                        continue

                    for pet in itertools.permutations(pets):
                        # Spaniard owns dog (clue 3)
                        if pet[nation.index('Spaniard')] != 'dog':
                            continue
                        # Snail owner likes dancing (clue 7)
                        if hobby[pet.index('snail')] != 'dancing':
                            continue
                        # Yellow house is painter (clue 8)
                        if hobby[color.index('yellow')] != 'painter':
                            continue
                        # Norwegian next to blue house (clue 15) - must be house 2 (index 1)
                        if color[1] != 'blue':
                            continue
                        # Reader next to fox (clue 11)
                        if abs(hobby.index('reading') - pet.index('fox')) != 1:
                            continue
                        # Painter next to horse (clue 12)
                        if abs(hobby.index('painter') - pet.index('horse')) != 1:
                            continue

                        # Found valid solution
                        water_drinker = nation[drink.index('water')]
                        # Lookup owner by pet's position in solution
                        zebra_owner = next(nation[i] for i, p in enumerate(pet) if p == 'zebra')
                        yield {"drinks_water": water_drinker, "owns_zebra": zebra_owner}

# Precompute the solution
solution = iter(solution())
