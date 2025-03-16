def recite(start, take=1):
    verses = []
    current = start
    for _ in range(take):
        verses.append(current)
        current = current - 1 if current > 0 else 99
    lines = []
    for i, current in enumerate(verses):
        if current == 0:
            line1 = "No more bottles of beer on the wall, no more bottles of beer."
            line2 = "Go to the store and buy some more, 99 bottles of beer on the wall."
        else:
            bottle_str = "bottles" if current != 1 else "bottle"
            line1 = f"{current} {bottle_str} of beer on the wall, {current} {bottle_str} of beer."
            next_bottles = current - 1
            if next_bottles == 0:
                next_str = "no more bottles"
            else:
                next_bottle_str = "bottles" if next_bottles != 1 else "bottle"
                next_str = f"{next_bottles} {next_bottle_str}"
            action = "Take one down and pass it around" if current != 1 else "Take it down and pass it around"
            line2 = f"{action}, {next_str} of beer on the wall."
        lines.append(line1)
        lines.append(line2)
        if i != len(verses) - 1:
            lines.append("")
    return lines
