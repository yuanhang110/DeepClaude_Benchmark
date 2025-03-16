def recite(start_verse, end_verse):
    animals = [
        {  # 1 - fly
            "name": "fly",
            "intro_comment": None,
            "chain_suffix": ""
        },
        {  # 2 - spider
            "name": "spider",
            "intro_comment": "It wriggled and jiggled and tickled inside her.",
            "chain_suffix": " that wriggled and jiggled and tickled inside her"
        },
        {  # 3 - bird
            "name": "bird",
            "intro_comment": "How absurd to swallow a bird!",
            "chain_suffix": ""
        },
        {  # 4 - cat
            "name": "cat",
            "intro_comment": "Imagine that, to swallow a cat!",
            "chain_suffix": ""
        },
        {  # 5 - dog
            "name": "dog",
            "intro_comment": "What a hog, to swallow a dog!",
            "chain_suffix": ""
        },
        {  # 6 - goat
            "name": "goat",
            "intro_comment": "Just opened her throat and swallowed a goat!",
            "chain_suffix": ""
        },
        {  # 7 - cow
            "name": "cow",
            "intro_comment": "I don't know how she swallowed a cow!",
            "chain_suffix": ""
        }
    ]
    
    verses = []
    for verse_num in range(start_verse, end_verse + 1):
        verse_lines = []
        if verse_num == 8:
            verse_lines.append("I know an old lady who swallowed a horse.")
            verse_lines.append("She's dead, of course!")
        else:
            animal = animals[verse_num - 1]
            verse_lines.append(f"I know an old lady who swallowed a {animal['name']}.")
            
            if animal['intro_comment']:
                verse_lines.append(animal['intro_comment'])
            
            current_index = verse_num - 1
            while current_index > 0:
                prey_index = current_index - 1
                prey = animals[prey_index]
                line = f"She swallowed the {animals[current_index]['name']} to catch the {prey['name']}{prey['chain_suffix']}."
                verse_lines.append(line)
                current_index = prey_index
            
            verse_lines.append("I don't know why she swallowed the fly. Perhaps she'll die.")
        
        verses.append(verse_lines)
    
    # Join verses with empty lines between them
    result = []
    for i, verse in enumerate(verses):
        result.extend(verse)
        if i != len(verses) - 1:
            result.append("")
    
    return result
