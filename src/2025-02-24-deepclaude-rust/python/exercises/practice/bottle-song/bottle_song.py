def recite(start, take=1):
    number_words = {
        0: 'no',
        1: 'One',
        2: 'Two',
        3: 'Three',
        4: 'Four',
        5: 'Five',
        6: 'Six',
        7: 'Seven',
        8: 'Eight',
        9: 'Nine',
        10: 'Ten',
    }
    verses = []
    for i in range(take):
        current_num = start - i
        current_word = number_words.get(current_num, str(current_num))
        if current_num == 1:
            line1 = f"{current_word} green bottle hanging on the wall,"
        else:
            line1 = f"{current_word} green bottles hanging on the wall,"
        line2 = line1
        line3 = "And if one green bottle should accidentally fall,"
        next_num = current_num - 1
        next_word = number_words.get(next_num, str(next_num))
        if next_num != 0:
            next_word = next_word.lower()
        if next_num == 1:
            line4 = f"There'll be {next_word} green bottle hanging on the wall."
        else:
            line4 = f"There'll be {next_word} green bottles hanging on the wall."
        verses.extend([line1, line2, line3, line4])
        if i != take - 1:
            verses.append("")
    return verses
