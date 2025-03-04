def best_hands(hands):
    """Pick the best hand(s) from a list of poker hands.
    
    Args:
        hands: List of strings representing poker hands
        
    Returns:
        List of strings containing the best hand(s)
    """
    if not hands:
        return []
        
    # Parse all hands and calculate their rank values
    parsed_hands = []
    for hand in hands:
        cards = hand.split()
        values = sorted((card_value(card[0]) for card in cards), reverse=True)
        suits = [card[-1] for card in cards]
        is_flush = len(set(suits)) == 1
        is_straight = is_consecutive(values)
        
        # Count value occurrences for pairs, three/four of a kind
        value_counts = {}
        for v in values:
            value_counts[v] = value_counts.get(v, 0) + 1
        sorted_counts = sorted(value_counts.items(), key=lambda x: (x[1], x[0]), reverse=True)
        
        # Determine hand rank
        if is_straight and is_flush:
            # Special case for low straight flush (A-2-3-4-5)
            if values == [14, 5, 4, 3, 2]:
                rank = (8, [5])  # Treat as 5-high straight flush
            else:
                rank = (8, values)  # Straight flush
        elif sorted_counts[0][1] == 4:
            rank = (7, [sorted_counts[0][0]] + [x[0] for x in sorted_counts[1:]])  # Four of a kind
        elif sorted_counts[0][1] == 3 and sorted_counts[1][1] == 2:
            rank = (6, [sorted_counts[0][0], sorted_counts[1][0]])  # Full house
        elif is_flush:
            rank = (5, values)  # Flush
        elif is_straight:
            rank = (4, values)  # Straight
        elif sorted_counts[0][1] == 3:
            rank = (3, [sorted_counts[0][0]] + [x[0] for x in sorted_counts[1:]])  # Three of a kind
        elif sorted_counts[0][1] == 2 and sorted_counts[1][1] == 2:
            rank = (2, sorted([sorted_counts[0][0], sorted_counts[1][0]], reverse=True) + 
                    [x[0] for x in sorted_counts[2:]])  # Two pair
        elif sorted_counts[0][1] == 2:
            rank = (1, [sorted_counts[0][0]] + [x[0] for x in sorted_counts[1:]])  # One pair
        else:
            rank = (0, values)  # High card
            
        parsed_hands.append((rank, hand))
    
    # Find the maximum rank
    max_rank = max(parsed_hands, key=lambda x: x[0])[0]
    
    # Return all hands with the maximum rank
    return [hand for (rank, hand) in parsed_hands if rank == max_rank]

def card_value(card):
    """Convert card rank to numerical value."""
    if card == 'J':
        return 11
    elif card == 'Q':
        return 12
    elif card == 'K':
        return 13
    elif card == 'A':
        return 14
    elif card == 'T':
        return 10
    else:
        return int(card)

def is_consecutive(values):
    """Check if values are consecutive, handling Aces as both high and low."""
    # Handle low straight (A-2-3-4-5)
    if values == [14, 5, 4, 3, 2]:
        return True
    # Handle normal consecutive
    return all(values[i] == values[i+1] + 1 for i in range(len(values)-1))
