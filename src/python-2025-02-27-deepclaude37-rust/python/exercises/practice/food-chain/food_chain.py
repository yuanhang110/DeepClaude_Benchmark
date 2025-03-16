def recite(start_verse, end_verse):
    animals = [
        {
            'name': 'fly',
            'action': None,
            'prey_desc': 'fly',
            'ending': 'Perhaps she\'ll die.'
        },
        {
            'name': 'spider',
            'action': 'It wriggled and jiggled and tickled inside her.',
            'prey_desc': 'spider that wriggled and jiggled and tickled inside her',
            'ending': 'Perhaps she\'ll die.'
        },
        {
            'name': 'bird',
            'action': 'How absurd to swallow a bird!',
            'prey_desc': 'bird',
            'ending': 'Perhaps she\'ll die.'
        },
        {
            'name': 'cat',
            'action': 'Imagine that, to swallow a cat!',
            'prey_desc': 'cat',
            'ending': 'Perhaps she\'ll die.'
        },
        {
            'name': 'dog',
            'action': 'What a hog, to swallow a dog!',
            'prey_desc': 'dog',
            'ending': 'Perhaps she\'ll die.'
        },
        {
            'name': 'goat',
            'action': 'Just opened her throat and swallowed a goat!',
            'prey_desc': 'goat',
            'ending': 'Perhaps she\'ll die.'
        },
        {
            'name': 'cow',
            'action': 'I don\'t know how she swallowed a cow!',
            'prey_desc': 'cow',
            'ending': 'Perhaps she\'ll die.'
        },
        {
            'name': 'horse',
            'action': None,
            'prey_desc': None,
            'ending': 'She\'s dead, of course!'
        },
    ]
    
    verses = []
    for v in range(start_verse, end_verse + 1):
        index = v - 1
        animal = animals[index]
        lines = []
        
        # Add the initial line
        lines.append(f"I know an old lady who swallowed a {animal['name']}.")
        
        # Check if it's the horse
        if animal['name'] == 'horse':
            lines.append("She's dead, of course!")
        else:
            # Add action line if present
            if animal['action'] is not None:
                lines.append(animal['action'])
            
            # Add chain lines
            for j in range(index, 0, -1):
                current = animals[j]
                prey = animals[j-1]
                lines.append(f"She swallowed the {current['name']} to catch the {prey['prey_desc']}.")
            
            # Add ending lines
            lines.append("I don't know why she swallowed the fly. " + animal['ending'])
        
        verses.append(lines)
    
    # Flatten the list of verses into a single list of lines, with empty strings between verses
    result = []
    for i, verse in enumerate(verses):
        if i > 0:
            result.append('')  # separate verses with an empty line
        result.extend(verse)
    
    return result
