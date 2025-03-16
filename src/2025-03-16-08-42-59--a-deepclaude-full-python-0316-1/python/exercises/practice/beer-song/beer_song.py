def recite(start, take=1):
    """
    Recite the lyrics to "99 Bottles of Beer on the Wall".
    
    Args:
        start: The number of bottles to start with
        take: The number of verses to generate
    
    Returns:
        A list of strings representing the lyrics
    """
    lyrics = []
    
    end = max(1, start - take + 1)
    for bottle_num in range(start, end - 1, -1):
        # Only process positive bottle numbers in this loop
        if bottle_num <= 0:
            break
            
        # First line of verse
        if bottle_num == 1:
            lyrics.append(f"1 bottle of beer on the wall, 1 bottle of beer.")
        else:
            lyrics.append(f"{bottle_num} bottles of beer on the wall, {bottle_num} bottles of beer.")
        
        # Second line of verse
        next_bottle = bottle_num - 1
        if next_bottle == 0:
            lyrics.append("Take it down and pass it around, no more bottles of beer on the wall.")
        elif next_bottle == 1:
            lyrics.append(f"Take one down and pass it around, 1 bottle of beer on the wall.")
        else:
            lyrics.append(f"Take one down and pass it around, {next_bottle} bottles of beer on the wall.")
        
        # Add blank line between verses
        if bottle_num > start - take + 1:
            lyrics.append("")
    
    # Handle special case for "no more bottles"
    if start - take < 0 and start > 0:
        lyrics.append("")  # Add blank line before final verse only if we have previous verses
    
    if start - take + 1 <= 0:
        lyrics.append("No more bottles of beer on the wall, no more bottles of beer.")
        lyrics.append("Go to the store and buy some more, 99 bottles of beer on the wall.")
    
    return lyrics
