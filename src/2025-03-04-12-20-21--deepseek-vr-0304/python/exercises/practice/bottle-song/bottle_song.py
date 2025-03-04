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
        1: 'One',
        0: 'No'
    }
    
    verses_numbers = []
    current = start
    for _ in range(take):
        if current < 1:
            break
        verses_numbers.append(current)
        current -= 1
    
    lines = []
    for i, n in enumerate(verses_numbers):
        current_word = number_words[n]
        current_bottle = 'bottle' + ('s' if n != 1 else '')
        line1 = f"{current_word} green {current_bottle} hanging on the wall,"
        line2 = line1
        line3 = "And if one green bottle should accidentally fall,"
        
        next_num = n - 1
        if next_num == 0:
            next_word = 'no'
            next_bottle = 'bottles'
        else:
            next_word = number_words[next_num].lower()
            next_bottle = 'bottle' + ('s' if next_num != 1 else '')
        line4 = f"There'll be {next_word} green {next_bottle} hanging on the wall."
        
        lines.extend([line1, line2, line3, line4])
        if i != len(verses_numbers) - 1:
            lines.append("")
    
    return lines
