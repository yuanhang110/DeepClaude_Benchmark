def best_hands(hands):
    def parse_hand(hand):
        cards = hand.split()
        ranks = []
        suits = []
        for card in cards:
            suit = card[-1].upper()
            rank_str = card[:-1].upper()
            if rank_str == 'J':
                rank = 11
            elif rank_str == 'Q':
                rank = 12
            elif rank_str == 'K':
                rank = 13
            elif rank_str == 'A':
                rank = 14
            elif rank_str == '10':
                rank = 10
            else:
                rank = int(rank_str)
            ranks.append(rank)
            suits.append(suit)
        return ranks, suits

    def evaluate_hand(ranks, suits):
        from collections import Counter
        count = Counter(ranks)
        sorted_counts = sorted(count.values(), reverse=True)
        sorted_ranks = sorted(ranks, reverse=True)
        flush = len(set(suits)) == 1
        
        # Check straight
        unique_ranks = sorted(count.keys())
        straight = False
        high_card = None
        if len(unique_ranks) == 5:
            if max(unique_ranks) - min(unique_ranks) == 4:
                straight = True
                high_card = max(unique_ranks)
            elif set(unique_ranks) == {14, 2, 3, 4, 5}:
                straight = True
                high_card = 5
        
        if flush and straight:
            return (8, high_card)
        if sorted_counts[0] == 4:
            quad_rank = [r for r, c in count.items() if c == 4][0]
            kicker = [r for r in sorted_ranks if r != quad_rank][0]
            return (7, quad_rank, kicker)
        if sorted_counts[0] == 3 and sorted_counts[1] == 2:
            three_rank = [r for r, c in count.items() if c == 3][0]
            pair_rank = [r for r, c in count.items() if c == 2][0]
            return (6, three_rank, pair_rank)
        if flush:
            return (5,) + tuple(sorted_ranks)
        if straight:
            return (4, high_card)
        if sorted_counts[0] == 3:
            three_rank = [r for r, c in count.items() if c == 3][0]
            others = sorted([r for r in sorted_ranks if r != three_rank], reverse=True)
            return (3, three_rank, others[0], others[1])
        if sorted_counts[0] == 2 and sorted_counts[1] == 2:
            pairs = sorted([r for r, c in count.items() if c == 2], reverse=True)
            kicker = [r for r in sorted_ranks if r not in pairs][0]
            return (2, pairs[0], pairs[1], kicker)
        if sorted_counts[0] == 2:
            pair_rank = [r for r, c in count.items() if c == 2][0]
            others = sorted([r for r in sorted_ranks if r != pair_rank], reverse=True)
            return (1, pair_rank, others[0], others[1], others[2])
        return (0,) + tuple(sorted_ranks)

    max_rank = None
    best = []
    for hand in hands:
        ranks, suits = parse_hand(hand)
        current_rank = evaluate_hand(ranks, suits)
        if max_rank is None or current_rank > max_rank:
            max_rank = current_rank
            best = [hand]
        elif current_rank == max_rank:
            best.append(hand)
    return best
