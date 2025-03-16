def proverb(*rhyme_items, qualifier=None):
    if not rhyme_items:
        return []
    
    lines = []
    for current, next_item in zip(rhyme_items, rhyme_items[1:]):
        lines.append(f"For want of a {current} the {next_item} was lost.")
    
    if qualifier:
        lines.append(f"And all for the want of a {qualifier} {rhyme_items[0]}.")
    else:
        lines.append(f"And all for the want of a {rhyme_items[0]}.")
    
    return lines
