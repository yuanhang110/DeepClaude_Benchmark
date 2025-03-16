def best_hands(hands):
    """
    Pick the best hand(s) from a list of poker hands.
    
    :param hands: List of poker hand strings
    :return: List of the best hand(s)
    """
    # Define card ranks
    ranks = {'2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9, 
             'T': 10, 'J': 11, 'Q': 12, 'K': 13, 'A': 14}
    
    def parse_hand(hand):
        """Parse a hand string into cards with rank and suit."""
        cards = hand.split()
        card_values = []
        for card in cards:
            if len(card) == 3 and card.startswith('10'):  # Handle '10' specially
                rank, suit = 'T', card[2]
            else:
                rank, suit = card[0], card[1]
            card_values.append((ranks[rank], suit))
        return card_values
    
    def hand_rank(hand):
        """
        Return a tuple representing the hand's rank.
        Higher tuples represent better hands.
        """
        cards = parse_hand(hand)
        card_ranks = [rank for rank, _ in cards]
        card_suits = [suit for _, suit in cards]
        
        # Count occurrences of each rank
        rank_counts = {}
        for r in card_ranks:
            rank_counts[r] = rank_counts.get(r, 0) + 1
        
        # Sort counts for easy access (highest count first, then highest rank)
        counts = sorted([(count, rank) for rank, count in rank_counts.items()], 
                       reverse=True)
        
        # Extract ranks by frequency for comparison
        ranks_by_count = []
        for count, rank in counts:
            ranks_by_count.append(rank)
        
        # Check for flush (all same suit)
        is_flush = len(set(card_suits)) == 1
        
        # Check for straight (consecutive ranks)
        distinct_ranks = sorted(set(card_ranks))
        is_straight = (len(distinct_ranks) == 5 and 
                      max(distinct_ranks) - min(distinct_ranks) == 4)
        
        # For comparing straights
        straight_high = max(distinct_ranks)
        
        # Special case: A-5-4-3-2 straight (5-high straight)
        if sorted(card_ranks) == [2, 3, 4, 5, 14]:
            is_straight = True
            straight_high = 5  # Ace is low in this case, so high card is 5
        
        # Determine hand type and return appropriate ranking tuple
        if is_straight and is_flush:
            return (8, straight_high)  # Straight flush
        elif counts[0][0] == 4:
            return (7, counts[0][1], counts[1][1])  # Four of a kind
        elif counts[0][0] == 3 and counts[1][0] == 2:
            return (6, counts[0][1], counts[1][1])  # Full house
        elif is_flush:
            return (5, sorted(card_ranks, reverse=True))  # Flush
        elif is_straight:
            return (4, straight_high)  # Straight
        elif counts[0][0] == 3:
            return (3, counts[0][1], *sorted([r for r in card_ranks if r != counts[0][1]], reverse=True))  # Three of a kind
        elif counts[0][0] == 2 and counts[1][0] == 2:
            kicker = [r for r in card_ranks if r != counts[0][1] and r != counts[1][1]][0]
            return (2, max(counts[0][1], counts[1][1]), min(counts[0][1], counts[1][1]), kicker)  # Two pair
        elif counts[0][0] == 2:
            return (1, counts[0][1], *sorted([r for r in card_ranks if r != counts[0][1]], reverse=True))  # One pair
        else:
            return (0, *sorted(card_ranks, reverse=True))  # High card
    
    # Rank all hands
    ranked_hands = [(hand_rank(hand), hand) for hand in hands]
    
    # Find the maximum rank
    max_rank = max(ranked_hands, key=lambda x: x[0])[0]
    
    # Return all hands with the maximum rank
    return [hand for rank, hand in ranked_hands if rank == max_rank]
