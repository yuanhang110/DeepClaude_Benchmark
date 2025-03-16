def best_hands(hands):
    def evaluate_hand(hand):
        cards = []
        for card_str in hand.split():
            value_str = card_str[:-1]
            suit = card_str[-1].upper()
            value = {
                '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9, '10': 10,
                'J': 11, 'Q': 12, 'K': 13, 'A': 14
            }[value_str.upper()]
            cards.append((value, suit))
        values = [v for v, s in cards]
        suits = [s for v, s in cards]
        values.sort()
        
        # Check for flush
        is_flush = all(s == suits[0] for s in suits)
        
        # Check for straight
        unique = len(set(values)) == 5
        is_straight = False
        if unique and (values[-1] - values[0] == 4):
            is_straight = True
        elif values == [2, 3, 4, 5, 14]:
            is_straight = True
        
        # Check for straight flush
        if is_straight and is_flush:
            if values == [2, 3, 4, 5, 14]:
                return (8, 5)  # Ace-low straight flush
            else:
                return (8, values[-1])
        
        # Count occurrences of each value
        counts = {}
        for v in values:
            counts[v] = counts.get(v, 0) + 1
        groups = sorted(counts.items(), key=lambda x: (-x[1], -x[0]))
        count_values = [g[1] for g in groups]
        ordered_values = [g[0] for g in groups]
        
        # Four of a kind
        if count_values[0] == 4:
            return (7, ordered_values[0], ordered_values[1])
        
        # Full house
        if count_values[0] == 3 and count_values[1] == 2:
            return (6, ordered_values[0], ordered_values[1])
        
        # Flush
        if is_flush:
            return (5, *sorted(values, reverse=True))
        
        # Straight
        if is_straight:
            if values == [2, 3, 4, 5, 14]:
                return (4, 5)  # Ace-low straight
            else:
                return (4, values[-1])
        
        # Three of a kind
        if count_values[0] == 3:
            return (3, ordered_values[0], *ordered_values[1:])
        
        # Two pairs
        if count_values[0] == 2 and count_values[1] == 2:
            return (2, ordered_values[0], ordered_values[1], ordered_values[2])
        
        # One pair
        if count_values[0] == 2:
            return (1, ordered_values[0], *ordered_values[1:])
        
        # High card
        return (0, *sorted(values, reverse=True))
    
    # Evaluate all hands and find the best ones
    evaluated = []
    for hand in hands:
        score = evaluate_hand(hand)
        evaluated.append((score, hand))
    
    max_score = max(evaluated, key=lambda x: x[0])[0]
    best = [hand for score, hand in evaluated if score == max_score]
    return best
