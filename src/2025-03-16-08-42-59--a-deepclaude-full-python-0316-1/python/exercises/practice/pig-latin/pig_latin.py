def translate(text):
    """
    Translate text from English to Pig Latin according to the rules:
    1. Words beginning with vowels or 'xr'/'yt' -> add 'ay' to the end
    2. Words beginning with consonants -> move consonants to end and add 'ay'
    3. Words with consonants + 'qu' -> move consonants and 'qu' to end and add 'ay'
    4. Words with consonants + 'y' -> move consonants before 'y' to end and add 'ay'
    """
    words = text.split()
    translated_words = []
    
    for word in words:
        translated_words.append(translate_word(word))
    
    return ' '.join(translated_words)

def translate_word(word):
    """Translate a single word to Pig Latin"""
    vowels = 'aeiou'
    
    # Rule 1: Word begins with a vowel or 'xr' or 'yt'
    if word[0].lower() in vowels or word.lower().startswith(('xr', 'yt')):
        return word + 'ay'
    
    # Rule 3: Word starts with consonants (if any) followed by 'qu'
    qu_index = word.lower().find('qu')
    if qu_index >= 0 and all(c.lower() not in vowels for c in word[:qu_index]):
        return word[qu_index+2:] + word[:qu_index+2] + 'ay'
    
    # Rule 4: Word starts with consonants followed by 'y'
    for i in range(1, len(word)):
        if word[i].lower() == 'y' and all(c.lower() not in vowels for c in word[:i]):
            return word[i:] + word[:i] + 'ay'
    
    # Rule 2: Word begins with one or more consonants
    for i in range(len(word)):
        if word[i].lower() in vowels:
            return word[i:] + word[:i] + 'ay'
    
    # If we get here, the word has no vowels
    return word + 'ay'
