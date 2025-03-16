def best_hands(hands):
    """Return a list of the best hands from a list of poker hands.
    
    Each hand is represented as a space-separated string of 5 cards.
    Each card is represented by its rank (2-9, T, J, Q, K, A) followed by its suit (S, H, D, C).
    """
    # Define a function to rank a hand
    def hand_rank(hand):
        # Convert hand string to a list of cards
        cards = hand.split()
        
        # Get ranks and suits (handle 10 which has 2-character rank)
        ranks = []
        suits = []
        for card in cards:
            if card.startswith('10'):
                ranks.append('10')
                suits.append(card[2])
            else:
                ranks.append(card[0])
                suits.append(card[1])
        
        # Convert face cards to numeric values
        rank_values = {'2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9, 
                      '10': 10, 'J': 11, 'Q': 12, 'K': 13, 'A': 14}
        
        # Convert ranks to numeric values and sort in descending order
        rank_nums = sorted([rank_values[r] for r in ranks], reverse=True)
        
        # Check for flush (all cards of the same suit)
        is_flush = len(set(suits)) == 1
        
        # Check for straight (consecutive ranks)
        is_straight = False
        if len(set(rank_nums)) == 5:  # All ranks are different
            if rank_nums[0] - rank_nums[4] == 4:  # Regular straight
                is_straight = True
            # Special case: A-5-4-3-2 straight (Ace is treated as 1)
            elif rank_nums == [14, 5, 4, 3, 2]:
                is_straight = True
                # For A-5-4-3-2 straight, the high card is 5, not A
                rank_nums = [5, 4, 3, 2, 1]
        
        # Count occurrences of each rank
        rank_counts = {}
        for r in rank_nums:
            if r in rank_counts:
                rank_counts[r] += 1
            else:
                rank_counts[r] = 1
        
        # Sort by count (descending), then by rank (descending)
        count_rank_pairs = sorted([(count, rank) for rank, count in rank_counts.items()], 
                                 key=lambda x: (x[0], x[1]), reverse=True)
        
        # Extract counts and ranks separately
        counts = [count for count, _ in count_rank_pairs]
        count_ranks = [rank for _, rank in count_rank_pairs]
        
        # Determine hand category and return appropriate ranking tuple
        
        # Straight flush (including royal flush)
        if is_straight and is_flush:
            return (8, rank_nums)
        
        # Four of a kind
        if counts == [4, 1]:
            return (7, count_ranks)
        
        # Full house
        if counts == [3, 2]:
            return (6, count_ranks)
        
        # Flush
        if is_flush:
            return (5, rank_nums)
        
        # Straight
        if is_straight:
            return (4, rank_nums)
        
        # Three of a kind
        if counts == [3, 1, 1]:
            return (3, count_ranks)
        
        # Two pair
        if counts == [2, 2, 1]:
            return (2, count_ranks)
        
        # One pair
        if counts == [2, 1, 1, 1]:
            return (1, count_ranks)
        
        # High card
        return (0, rank_nums)
    
    # Rank all hands
    ranked_hands = [(hand_rank(hand), hand) for hand in hands]
    
    # Find the maximum rank
    max_rank = max(ranked_hands, key=lambda x: x[0])[0]
    
    # Return all hands with the maximum rank
    return [hand for rank, hand in ranked_hands if rank == max_rank]
