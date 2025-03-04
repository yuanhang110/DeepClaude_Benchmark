def recite(start_verse, end_verse):
    animals = [
        {'name': 'fly', 'comment': "I don't know why she swallowed the fly. Perhaps she'll die.", 'suffix': ''},
        {'name': 'spider', 'comment': 'It wriggled and jiggled and tickled inside her.', 'suffix': ' that wriggled and jiggled and tickled inside her'},
        {'name': 'bird', 'comment': 'How absurd to swallow a bird!', 'suffix': ''},
        {'name': 'cat', 'comment': 'Imagine that, to swallow a cat!', 'suffix': ''},
        {'name': 'dog', 'comment': 'What a hog, to swallow a dog!', 'suffix': ''},
        {'name': 'goat', 'comment': 'Just opened her throat and swallowed a goat!', 'suffix': ''},
        {'name': 'cow', 'comment': "I don't know how she swallowed a cow!", 'suffix': ''},
        {'name': 'horse', 'comment': "She's dead, of course!", 'suffix': ''},
    ]
    
    all_lines = []
    for verse_num in range(start_verse, end_verse + 1):
        if verse_num == 8:
            verse_lines = [
                "I know an old lady who swallowed a horse.",
                "She's dead, of course!"
            ]
        else:
            current_index = verse_num - 1
            animal = animals[current_index]
            verse_lines = [
                f"I know an old lady who swallowed a {animal['name']}.",
                animal['comment']
            ]
            if current_index > 0:
                for j in range(current_index, 0, -1):
                    predator = animals[j]
                    prey = animals[j-1]
                    verse_lines.append(f"She swallowed the {predator['name']} to catch the {prey['name']}{prey['suffix']}.")
                verse_lines.append(animals[0]['comment'])
        # Add empty line between verses
        if all_lines and verse_lines:
            all_lines.append('')
        all_lines.extend(verse_lines)
    return all_lines
