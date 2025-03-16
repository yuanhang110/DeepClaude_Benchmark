def recite(start_verse, end_verse):
    animals = [
        ("fly", "I don't know why she swallowed the fly. Perhaps she'll die."),
        ("spider", "It wriggled and jiggled and tickled inside her."),
        ("bird", "How absurd to swallow a bird!"),
        ("cat", "Imagine that, to swallow a cat!"),
        ("dog", "What a hog, to swallow a dog!"),
        ("goat", "Just opened her throat and swallowed a goat!"),
        ("cow", "I don't know how she swallowed a cow!"),
        ("horse", "She's dead, of course!")
    ]
    
    result = []
    
    for verse_num in range(start_verse, end_verse + 1):
        animal_index = verse_num - 1
        animal, comment = animals[animal_index]
        
        # Start verse with standard intro
        result.append(f"I know an old lady who swallowed a {animal}.")
        result.append(comment)
        
        # For the horse (last verse), we don't add the chain
        if animal == "horse":
            # Add blank line after verse unless it's the last verse
            if verse_num != end_verse:
                result.append("")
            continue
        
        # Add the chain of animals she swallowed (skip for fly)
        if animal != "fly":
            for i in range(animal_index, 0, -1):
                current = animals[i][0]
                previous = animals[i-1][0]
                
                # Special handling for spider with its extended description
                if previous == "spider":
                    result.append(f"She swallowed the {current} to catch the {previous} that wriggled and jiggled and tickled inside her.")
                else:
                    result.append(f"She swallowed the {current} to catch the {previous}.")
            
            # Add the final line about the fly
            result.append(animals[0][1])
        
        # Add blank line after verse unless it's the last verse
        if verse_num != end_verse:
            result.append("")
    
    return result
