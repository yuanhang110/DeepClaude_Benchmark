def recite(start, take=1):
    verses = []
    current = start
    for i in range(take):
        # Generate first line
        if current == 0:
            line1 = "No more bottles of beer on the wall, no more bottles of beer."
        else:
            bottles = "bottle" if current == 1 else "bottles"
            line1 = f"{current} {bottles} of beer on the wall, {current} {bottles} of beer."
        
        # Generate second line
        if current == 0:
            line2 = "Go to the store and buy some more, 99 bottles of beer on the wall."
            next_n = 99
        else:
            next_n = current - 1
            action = "Take it down and pass it around" if current == 1 else "Take one down and pass it around"
            if next_n == 0:
                next_line = "no more bottles of beer on the wall."
            else:
                bottles_next = "bottle" if next_n == 1 else "bottles"
                next_line = f"{next_n} {bottles_next} of beer on the wall."
            line2 = f"{action}, {next_line}"
        
        # Add lines to verses
        verses.append(line1)
        verses.append(line2)
        
        # Add empty string between verses except after last
        if i != take - 1:
            verses.append("")
        
        current = next_n
    
    return verses
