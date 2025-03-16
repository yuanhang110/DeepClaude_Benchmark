def recite(start, take=1):
    """
    Generate the lyrics for the '99 Bottles of Beer on the Wall' song.
    
    Args:
        start: The number of bottles to start with
        take: The number of verses to generate
    
    Returns:
        A list of strings representing the lyrics
    """
    result = []
    
    for i in range(start, start - take, -1):
        # First line of verse
        if i == 0:
            result.append("No more bottles of beer on the wall, no more bottles of beer.")
        elif i == 1:
            result.append("1 bottle of beer on the wall, 1 bottle of beer.")
        else:
            result.append(f"{i} bottles of beer on the wall, {i} bottles of beer.")
        
        # Second line of verse
        if i == 0:
            result.append("Go to the store and buy some more, 99 bottles of beer on the wall.")
        elif i == 1:
            result.append("Take it down and pass it around, no more bottles of beer on the wall.")
        elif i == 2:
            result.append("Take one down and pass it around, 1 bottle of beer on the wall.")
        else:
            result.append(f"Take one down and pass it around, {i-1} bottles of beer on the wall.")
        
        # Add blank line between verses except after the last verse
        if i > start - take + 1:
            result.append("")
    
    return result
