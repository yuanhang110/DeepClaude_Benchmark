def proverb(*items, qualifier=None):
    if not items:
        return []
    
    verses = []
    for i in range(len(items) - 1):
        verses.append(f"For want of a {items[i]} the {items[i+1]} was lost.")
    
    final = f"And all for the want of a {qualifier + ' ' if qualifier else ''}{items[0]}."
    verses.append(final)
    
    return verses
