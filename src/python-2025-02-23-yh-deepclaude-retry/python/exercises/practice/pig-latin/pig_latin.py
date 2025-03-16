def translate(text):
    vowels = {'a', 'e', 'i', 'o', 'u'}
    translated = []
    for word in text.split():
        if word.startswith(('xr', 'yt')) or word[0] in vowels:
            translated.append(word + 'ay')
            continue
        
        qu_pos = -1
        for i in range(len(word)-1):
            if word[i:i+2] == 'qu' and all(c not in vowels for c in word[:i]):
                qu_pos = i
                break
        if qu_pos != -1:
            translated.append(word[qu_pos+2:] + word[:qu_pos+2] + 'ay')
            continue
        
        y_pos = word.find('y')
        if y_pos != -1 and y_pos > 0 and all(c not in vowels for c in word[:y_pos]):
            translated.append(word[y_pos:] + word[:y_pos] + 'ay')
            continue
        
        split = 0
        while split < len(word) and word[split] not in vowels:
            split += 1
        translated.append(word[split:] + word[:split] + 'ay')
    
    return ' '.join(translated)
