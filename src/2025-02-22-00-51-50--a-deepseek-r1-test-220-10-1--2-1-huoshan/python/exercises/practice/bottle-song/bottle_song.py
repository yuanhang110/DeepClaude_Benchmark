def recite(start, take=1):
    verses = []
    number_words = ["Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"]
    
    for current in range(start, start - take, -1):
        if current < 1:
            break
        
        # Generate current bottle phrase
        if current == 1:
            current_phrase = "One green bottle"
        elif 2 <= current <= 10:
            current_phrase = f"{number_words[current - 2]} green bottles"
        else:
            current_phrase = f"{current} green bottles"
        
        # Generate next bottle phrase
        next_n = current - 1
        if next_n == 0:
            next_phrase = "no green bottles"
        elif next_n == 1:
            next_phrase = "one green bottle"
        elif 2 <= next_n <= 10:
            next_phrase = f"{number_words[next_n - 2].lower()} green bottles"
        else:
            next_phrase = f"{next_n} green bottles"
        
        verse = [
            f"{current_phrase} hanging on the wall,",
            f"{current_phrase} hanging on the wall,",
            "And if one green bottle should accidentally fall,",
            f"There'll be {next_phrase} hanging on the wall."
        ]
        verses.append(verse)
    
    # Flatten verses with empty line separators
    lines = []
    for i, verse in enumerate(verses):
        lines.extend(verse)
        if i != len(verses) - 1:
            lines.append("")
    
    return lines
