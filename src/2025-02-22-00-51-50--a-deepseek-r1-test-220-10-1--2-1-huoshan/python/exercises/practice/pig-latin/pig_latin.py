def translate(text):
    def starts_with_vowel_sound(word):
        vowels = 'aeiou'
        return (len(word) > 0 and word[0] in vowels) or word.startswith('xr') or word.startswith('yt')

    def apply_rule3(word):
        # Check if the word starts with consonants followed by 'qu'
        for i in range(len(word)):
            if i <= len(word) - 2 and word[i:i+2] == 'qu':
                # Check if all characters before 'qu' are consonants
                prefix = word[:i]
                if all(c not in 'aeiou' for c in prefix):
                    return word[i+2:] + prefix + 'qu' + 'ay'
        return None

    def apply_rule4(word):
        # Find the first 'y' after consonants
        for i in range(1, len(word)):  # Start from 1 since y needs preceding consonants
            if word[i] == 'y':
                prefix = word[:i]
                if all(c not in 'aeiou' for c in prefix):
                    return word[i:] + prefix + 'ay'
        return None

    translated_words = []
    for word in text.split():
        if starts_with_vowel_sound(word):
            translated_words.append(word + 'ay')
            continue
        
        # Try rule 3 (consonants + qu)
        rule3_result = apply_rule3(word)
        if rule3_result is not None:
            translated_words.append(rule3_result)
            continue
        
        # Try rule 4 (consonants + y)
        rule4_result = apply_rule4(word)
        if rule4_result is not None:
            translated_words.append(rule4_result)
            continue
        
        # Apply rule 2 (move consonants)
        for i, c in enumerate(word):
            if c in 'aeiou':
                translated_words.append(word[i:] + word[:i] + 'ay')
                break
        else:  # No vowels found
            translated_words.append(word + 'ay')
    
    return ' '.join(translated_words)
