def translate(text):
    def get_split_idx(word):
        vowels = {'a', 'e', 'i', 'o', 'u'}
        
        # Rule 1: If word begins with vowel, 'xr', or 'yt'
        if word[0] in vowels or word.startswith('xr') or word.startswith('yt'):
            return 0
            
        # Rule 3: If word has consonants (possibly zero) followed by 'qu'
        for i in range(len(word) - 1):
            if word[i] == 'q' and word[i+1] == 'u':
                return i + 2  # Split after 'qu'
                
        # Find first occurrence of a vowel or 'y'
        first_vowel_or_y = None
        for i, c in enumerate(word):
            if c in vowels or c == 'y':
                first_vowel_or_y = i
                break
        
        # Rule 4: Check if first is 'y' and preceded by consonants
        if first_vowel_or_y is not None and word[first_vowel_or_y] == 'y' and first_vowel_or_y > 0:
            return first_vowel_or_y
        
        # Rule 2: Split at first vowel (if found)
        if first_vowel_or_y is not None:
            return first_vowel_or_y
        
        # Edge case: if no vowels found
        return len(word)
        
    def translate_word(word):
        split_idx = get_split_idx(word)
        return word[split_idx:] + word[:split_idx] + 'ay'
        
    # Process each word in the text
    return ' '.join(translate_word(word) for word in text.split())
