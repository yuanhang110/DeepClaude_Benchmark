def recite(start, take=1):
    verses = []
    number_words = {
        10: "Ten", 9: "Nine", 8: "Eight", 7: "Seven", 6: "Six",
        5: "Five", 4: "Four", 3: "Three", 2: "Two", 1: "One", 0: "No"
    }
    
    for i in range(take):
        current_num = start - i
        next_num = current_num - 1
        
        if current_num < 1:
            break
            
        current_word = number_words[current_num]
        next_word = number_words[next_num]
        
        current_bottle = "bottle" if current_num == 1 else "bottles"
        next_bottle = "bottle" if next_num == 1 else "bottles"
        
        verse = [
            f"{current_word} green {current_bottle} hanging on the wall,",
            f"{current_word} green {current_bottle} hanging on the wall,",
            f"And if one green bottle should accidentally fall,",
            f"There'll be {next_word.lower()} green {next_bottle} hanging on the wall."
        ]
        
        verses.extend(verse)
        if i < take - 1 and current_num > 1:
            verses.append("")
    
    return verses
