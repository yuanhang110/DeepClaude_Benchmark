def best_hands(hands):
    def parse_hand(hand):
        cards = hand.split()
        parsed = []
        rank_order = {'2': 2, '3':3, '4':4, '5':5, '6':6, '7':7, 
                      '8':8, '9':9, '10':10, 'J':11, 'Q':12, 'K':13, 'A':14}
        for card in cards:
            rank_str = card[:-1]
            suit = card[-1]
            rank = rank_order[rank_str.upper()]
            parsed.append( (rank, suit) )
        # Sort by rank descending, then suit ascending
        parsed.sort(key=lambda x: (-x[0], x[1]))
        return parsed

    def evaluate_hand(parsed):
        ranks = [c[0] for c in parsed]
        suits = [c[1] for c in parsed]

        # Count occurrences of each rank
        from collections import defaultdict
        count = defaultdict(int)
        for r in ranks:
            count[r] += 1
        # Sort by frequency descending, then rank descending
        sorted_counts = sorted(count.items(), key=lambda x: (-x[1], -x[0]))
        groups = [item[0] for item in sorted_counts]

        # Check for flush
        is_flush = len(set(suits)) == 1

        # Check for straight
        # Sort ranks in descending order (already sorted in parsed)
        unique_ranks = sorted(list(set(ranks)), reverse=True)
        is_straight = False
        high_card = unique_ranks[0]
        if len(unique_ranks) >=5:
            # Check normal straight
            for i in range(len(unique_ranks)-4):
                if (unique_ranks[i] - unique_ranks[i+4] ==4):
                    is_straight = True
                    high_card = unique_ranks[i]
                    break
        # Check Ace-low straight (A-2-3-4-5)
        if not is_straight and set(ranks) == {14,2,3,4,5}:
            is_straight = True
            high_card =5

        # Evaluate hand type
        if is_straight and is_flush:
            return (8, high_card)
        elif sorted_counts[0][1] ==4:
            # Four of a kind
            return (7, sorted_counts[0][0], sorted_counts[1][0])
        elif sorted_counts[0][1] ==3 and sorted_counts[1][1] ==2:
            # Full house
            return (6, sorted_counts[0][0], sorted_counts[1][0])
        elif is_flush:
            # Flush
            return (5, groups)
        elif is_straight:
            # Straight
            return (4, high_card)
        elif sorted_counts[0][1] ==3:
            # Three of a kind
            kickers = [g for g in groups if g != sorted_counts[0][0]]
            return (3, [sorted_counts[0][0]] + kickers)
        elif sorted_counts[0][1] ==2 and sorted_counts[1][1] ==2:
            # Two pairs
            pair1, pair2 = sorted_counts[0][0], sorted_counts[1][0]
            kicker = [g for g in groups if g not in (pair1, pair2)]
            return (2, [pair1, pair2] + kicker)
        elif sorted_counts[0][1] ==2:
            # One pair
            pair = sorted_counts[0][0]
            kickers = [g for g in groups if g != pair]
            return (1, [pair] + kickers)
        else:
            # High card
            return (0, groups)

    # Evaluate all hands and determine the best
    evaluated = []
    for hand in hands:
        parsed = parse_hand(hand)
        evaluated_hand = evaluate_hand(parsed)
        evaluated.append( (evaluated_hand, hand) )

    # Find the maximum evaluated hand
    max_rank = max(evaluated, key=lambda x: x[0])[0]
    winners = [hand for (rank, hand) in evaluated if rank == max_rank]

    return winners
