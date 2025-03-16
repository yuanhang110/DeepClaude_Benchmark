import re

def translate(text):
    translated_words = []
    for word in text.split():
        if re.match(r'^([aeiou]|xr|yt)', word):
            translated = word + 'ay'
        elif re.match(r'^[^aeiou]*qu', word):
            translated = re.sub(r'^([^aeiou]*qu)(.*)', r'\2\1ay', word)
        elif re.match(r'^[^aeiou]+y', word):
            translated = re.sub(r'^([^aeiou]+)y(.*)', r'y\2\1ay', word)
        else:
            translated = re.sub(r'^([^aeiou]+)(.*)', r'\2\1ay', word)
        translated_words.append(translated)
    return ' '.join(translated_words)
