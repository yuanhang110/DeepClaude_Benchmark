import re

def translate(text):
    def translate_word(word):
        # Rule 1: vowel, xr, yt at start
        if re.match(r'^([aeiou]|xr|yt)', word):
            return word + 'ay'
        # Rule 3: consonants followed by qu
        match = re.match(r'^([^aeiou]*qu)(.*)', word)
        if match:
            return match.group(2) + match.group(1) + 'ay'
        # Rule 4: consonants followed by y
        match = re.match(r'^([^aeiou]+)(y.*)', word)
        if match:
            return match.group(2) + match.group(1) + 'ay'
        # Rule 2: consonants only
        match = re.match(r'^([^aeiou]+)(.*)', word)
        if match:
            return match.group(2) + match.group(1) + 'ay'
        return word + 'ay'  # fallback
    
    return ' '.join(translate_word(word) for word in text.split())
