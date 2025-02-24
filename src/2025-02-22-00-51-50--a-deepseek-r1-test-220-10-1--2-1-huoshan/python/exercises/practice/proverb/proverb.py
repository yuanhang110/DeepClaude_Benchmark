def proverb(*items, qualifier=None):
    verses = []
    # Generate consecutive pair verses using unpacking
    for current, next_item in zip(items, items[1:]):
        verses.append(f"For want of a {current} the {next_item} was lost.")
    
    # Add final verse with qualifier if specified
    if items:
        first_item = f"{qualifier} {items[0]}" if qualifier else items[0]
        verses.append(f"And all for the want of a {first_item}.")
    
    return verses
