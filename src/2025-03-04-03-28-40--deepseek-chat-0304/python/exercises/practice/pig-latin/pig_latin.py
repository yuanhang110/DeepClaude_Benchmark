def translate(text):
    vowels = {'a', 'e', 'i', 'o', 'u'}
    result = []
    
    for word in text.split():
        # Rule 1: Starts with vowel or 'xr' or 'yt'
        if word[0] in vowels or word.startswith('xr') or word.startswith('yt'):
            result.append(word + 'ay')
            continue
            
        # Find the first vowel or 'y' position
        vowel_pos = 0
        while vowel_pos < len(word):
            if word[vowel_pos] in vowels or (vowel_pos > 0 and word[vowel_pos] == 'y'):
                break
            vowel_pos += 1
            
        # Rule 3: Handle 'qu' cases
        if vowel_pos > 0 and word[vowel_pos] == 'u' and word[vowel_pos-1] == 'q':
            vowel_pos += 1
            
        # Rule 2 & 4: Move consonants to end
        translated = word[vowel_pos:] + word[:vowel_pos] + 'ay'
        result.append(translated)
        
    return ' '.join(result)
