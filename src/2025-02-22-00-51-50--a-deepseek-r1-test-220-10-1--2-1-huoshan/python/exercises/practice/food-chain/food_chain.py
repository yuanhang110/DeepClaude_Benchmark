def recite(start_verse, end_verse):
    animals = [
        {  # Verse 1
            'name': 'fly',
            'intro': '',
            'action': ''
        },
        {  # Verse 2
            'name': 'spider',
            'intro': 'It wriggled and jiggled and tickled inside her.',
            'action': 'She swallowed the spider to catch the fly.'
        },
        {  # Verse 3
            'name': 'bird',
            'intro': 'How absurd to swallow a bird!',
            'action': 'She swallowed the bird to catch the spider that wriggled and jiggled and tickled inside her.'
        },
        {  # Verse 4
            'name': 'cat',
            'intro': 'Imagine that, to swallow a cat!',
            'action': 'She swallowed the cat to catch the bird.'
        },
        {  # Verse 5
            'name': 'dog',
            'intro': 'What a hog, to swallow a dog!',
            'action': 'She swallowed the dog to catch the cat.'
        },
        {  # Verse 6
            'name': 'goat',
            'intro': 'Just opened her throat and swallowed a goat!',
            'action': 'She swallowed the goat to catch the dog.'
        },
        {  # Verse 7
            'name': 'cow',
            'intro': 'I don\'t know how she swallowed a cow!',
            'action': 'She swallowed the cow to catch the goat.'
        }
    ]
    
    lyrics = []
    for verse_num in range(start_verse, end_verse + 1):
        if verse_num == 8:
            verse_lines = [
                "I know an old lady who swallowed a horse.",
                "She's dead, of course!"
            ]
        else:
            animal = animals[verse_num - 1]
            verse_lines = [f"I know an old lady who swallowed a {animal['name']}."]
            if animal['intro']:
                verse_lines.append(animal['intro'])
            
            # Add the chain of swallowing actions
            for prev_idx in range(verse_num - 1, 0, -1):
                verse_lines.append(animals[prev_idx]['action'])
            
            verse_lines.append("I don't know why she swallowed the fly. Perhaps she'll die.")
        
        # Add separator between verses
        if lyrics:
            lyrics.append("")
        lyrics.extend(verse_lines)
    
    return lyrics
