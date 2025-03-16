def recite(start_verse, end_verse):
    ANIMALS = [
        {
            'name': 'fly',
            'second_line': "I don't know why she swallowed the fly. Perhaps she'll die.",
            'suffix': ''
        },
        {
            'name': 'spider',
            'second_line': "It wriggled and jiggled and tickled inside her.",
            'suffix': ' that wriggled and jiggled and tickled inside her'
        },
        {
            'name': 'bird',
            'second_line': "How absurd to swallow a bird!",
            'suffix': ''
        },
        {
            'name': 'cat',
            'second_line': "Imagine that, to swallow a cat!",
            'suffix': ''
        },
        {
            'name': 'dog',
            'second_line': "What a hog, to swallow a dog!",
            'suffix': ''
        },
        {
            'name': 'goat',
            'second_line': "Just opened her throat and swallowed a goat!",
            'suffix': ''
        },
        {
            'name': 'cow',
            'second_line': "I don't know how she swallowed a cow!",
            'suffix': ''
        },
    ]

    def generate_verse(n):
        if n == 8:
            return [
                "I know an old lady who swallowed a horse.",
                "She's dead, of course!"
            ]
        animal = ANIMALS[n-1]
        lines = []
        lines.append(f"I know an old lady who swallowed a {animal['name']}.")
        lines.append(animal['second_line'])
        if n > 1:
            for i in range(n-1, 0, -1):
                predator = ANIMALS[i]
                prey = ANIMALS[i-1]
                lines.append(f"She swallowed the {predator['name']} to catch the {prey['name']}{prey['suffix']}.")
            lines.append(ANIMALS[0]['second_line'])
        return lines

    verses = []
    for n in range(start_verse, end_verse + 1):
        verses.append(generate_verse(n))
    
    lines = []
    for i, verse in enumerate(verses):
        lines.extend(verse)
        if i != len(verses) - 1:
            lines.append('')
    return lines
