def number_to_words(n):
    """Convert a number to its word representation."""
    num_words = ["Zero", "One", "Two", "Three", "Four", 
                "Five", "Six", "Seven", "Eight", "Nine", "Ten"]
    return num_words[n]

def recite(start, take=1):
    """Generate verses of the 'Ten Green Bottles' song."""
    verses = []
    for i in range(start, start - take, -1):
        current = number_to_words(i)
        next_num = number_to_words(i-1) if i > 1 else "no"
        
        verse = [
            f"{current} green bottle{'s' if i != 1 else ''} hanging on the wall,",
            f"{current} green bottle{'s' if i != 1 else ''} hanging on the wall,",
            "And if one green bottle should accidentally fall,",
            f"There'll be {next_num} green bottle{'s' if i-1 != 1 else ''} hanging on the wall."
        ]
        
        verses.extend(verse)
        if i > start - take + 1:
            verses.append("")
    
    return verses
