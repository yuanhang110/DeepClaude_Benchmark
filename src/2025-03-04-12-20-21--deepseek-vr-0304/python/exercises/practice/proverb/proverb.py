def proverb(*items, qualifier=None):
    verses = []
    for i in range(len(items) - 1):
        verses.append(f"For want of a {items[i]} the {items[i+1]} was lost.")
    
    if items:
        final_line = "And all for the want of a "
        if qualifier is not None:
            final_line += f"{qualifier} "
        final_line += f"{items[0]}."
        verses.append(final_line)
    
    return verses
