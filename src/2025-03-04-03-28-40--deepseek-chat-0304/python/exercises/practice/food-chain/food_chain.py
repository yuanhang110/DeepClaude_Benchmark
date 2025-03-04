def recite(start_verse, end_verse):
    """Generate the lyrics of 'I Know an Old Lady Who Swallowed a Fly'."""
    
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
    
    verses = []
    
    for i in range(start_verse - 1, end_verse):
        current_animal, comment = animals[i]
        verse = [f"I know an old lady who swallowed a {current_animal}."]
        
        if i == len(animals) - 1:  # Special case for horse
            verses.extend(verse)
            verses.append(comment)
            continue
            
        verses.extend(verse)
        verses.append(comment)
        
        if i > 0:
            for j in range(i, 0, -1):
                predator = animals[j][0]
                prey = animals[j-1][0]
                if prey == "spider":
                    verses.append(f"She swallowed the {predator} to catch the {prey} that wriggled and jiggled and tickled inside her.")
                else:
                    verses.append(f"She swallowed the {predator} to catch the {prey}.")
            
            verses.append(animals[0][1])
        
        # Add empty line between verses except for the last one
        if i < end_verse - 1:
            verses.append("")
    
    return verses
