def translate(text):
    words = text.split()
    translated_words = []
    
    for word in words:
        # Rule 1: Word begins with vowel or starts with "xr" or "yt"
        if word[0].lower() in 'aeiou' or word.lower().startswith(('xr', 'yt')):
            translated_words.append(word + 'ay')
        
        # Rule 3: Word starts with consonants (if any) followed by "qu"
        elif 'qu' in word.lower():
            qu_index = word.lower().index('qu')
            if qu_index == 0:
                # "qu" at the start
                translated_words.append(word[2:] + word[:2] + 'ay')
            else:
                # consonants followed by "qu"
                translated_words.append(word[qu_index+2:] + word[:qu_index+2] + 'ay')
        
        # Rule 4: Word starts with consonants followed by "y"
        elif 'y' in word.lower() and all(letter.lower() not in 'aeiou' for letter in word[:word.lower().index('y')]):
            y_index = word.lower().index('y')
            if y_index > 0:  # Make sure "y" is not the first letter
                translated_words.append(word[y_index:] + word[:y_index] + 'ay')
            else:
                # If "y" is the first letter, treat it as a consonant (Rule 2)
                translated_words.append(word[1:] + word[0] + 'ay')
        
        # Rule 2: Word begins with one or more consonants
        else:
            # Find the index of the first vowel
            first_vowel_index = next((i for i, letter in enumerate(word) if letter.lower() in 'aeiou'), len(word))
            translated_words.append(word[first_vowel_index:] + word[:first_vowel_index] + 'ay')
    
    return ' '.join(translated_words)
