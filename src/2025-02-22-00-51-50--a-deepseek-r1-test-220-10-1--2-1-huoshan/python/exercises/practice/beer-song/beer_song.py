def generate_verse(n):
    if n == 0:
        line1 = "No more bottles of beer on the wall, no more bottles of beer."
        line2 = "Go to the store and buy some more, 99 bottles of beer on the wall."
    else:
        plural = "bottle" if n == 1 else "bottles"
        line1 = f"{n} {plural} of beer on the wall, {n} {plural} of beer."
        
        if n == 1:
            action = "it"
            next_count = "no more"
            next_plural = "bottles"
        else:
            action = "one"
            next_count = n - 1
            if next_count == 0:
                next_count = "no more"
                next_plural = "bottles"
            else:
                next_plural = "bottle" if next_count == 1 else "bottles"
        
        line2 = f"Take {action} down and pass it around, {next_count} {next_plural} of beer on the wall."
    
    return f"{line1}\n{line2}"

def recite(start, take=1):
    verses = []
    for i in range(take):
        current = (start - i) % 100  # Handle wrap-around for numbers < 0
        verses.append(generate_verse(current))
    full_song = '\n\n'.join(verses)
    return full_song.split('\n')
