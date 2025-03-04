def proverb(*args, qualifier=None):
    if not args:
        return []
    
    verses = []
    for i in range(len(args) - 1):
        verses.append(f"For want of a {args[i]} the {args[i+1]} was lost.")
    
    final_verse = f"And all for the want of a {args[0]}."
    if qualifier:
        final_verse = f"And all for the want of a {qualifier} {args[0]}."
    
    verses.append(final_verse)
    return verses
