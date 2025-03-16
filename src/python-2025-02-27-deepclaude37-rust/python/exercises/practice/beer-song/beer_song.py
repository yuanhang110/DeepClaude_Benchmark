def recite(start, take=1):
    def verse(current):
        if current == 0:
            return [
                "No more bottles of beer on the wall, no more bottles of beer.",
                "Go to the store and buy some more, 99 bottles of beer on the wall."
            ]
        else:
            current_str = "1 bottle" if current == 1 else f"{current} bottles"
            line1 = f"{current_str} of beer on the wall, {current_str} of beer."
            
            next_bottles = current - 1
            action = "Take it down and pass it around, " if current == 1 else "Take one down and pass it around, "
            
            if next_bottles == 0:
                next_str = "no more bottles of beer on the wall."
            elif next_bottles == 1:
                next_str = "1 bottle of beer on the wall."
            else:
                next_str = f"{next_bottles} bottles of beer on the wall."
            line2 = action + next_str
            return [line1, line2]
    
    verses = []
    current = start
    for _ in range(take):
        verses.append(verse(current))
        current -= 1
        if current < 0:
            current = 99
    
    lines = []
    for i, verse_lines in enumerate(verses):
        lines.extend(verse_lines)
        if i != len(verses) - 1:
            lines.append("")
    
    return lines
