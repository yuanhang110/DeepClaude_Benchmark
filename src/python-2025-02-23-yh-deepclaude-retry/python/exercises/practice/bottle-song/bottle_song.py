def recite(start, take=1):
    number_words = {
        10: 'Ten',
        9: 'Nine',
        8: 'Eight',
        7: 'Seven',
        6: 'Six',
        5: 'Five',
        4: 'Four',
        3: 'Three',
        2: 'Two',
        1: 'One'
    }
    
    def get_verse(n):
        bottle_word = 'bottle' if n == 1 else 'bottles'
        line = f"{number_words[n]} green {bottle_word} hanging on the wall,"
        lines = [line, line]
        lines.append("And if one green bottle should accidentally fall,")
        next_n = n - 1
        if next_n == 0:
            next_str = 'no'
            bottle_word_next = 'bottles'
        else:
            next_str = number_words[next_n].lower()
            bottle_word_next = 'bottle' if next_n == 1 else 'bottles'
        lines.append(f"There'll be {next_str} green {bottle_word_next} hanging on the wall.")
        return '\n'.join(lines)
    
    verses = []
    current = start
    for _ in range(take):
        if current < 1:
            break
        verses.append(get_verse(current))
        current -= 1
    
    return '\n\n'.join(verses).split('\n')
